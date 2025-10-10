package org.example.java_training.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.example.java_training.dto.ListElementProductDTO;
import org.example.java_training.dto.ListProductWithCategoryDTO;

import java.io.Serializable;
import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "product")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SqlResultSetMapping(name = "ProductList", classes = {
        @ConstructorResult(targetClass = ListElementProductDTO.class, columns = {
                @ColumnResult(name = "id", type = Long.class),
                @ColumnResult(name = "name", type = String.class),
                @ColumnResult(name = "price", type = BigDecimal.class),
        })
})

@SqlResultSetMapping(name = "ProductWithCategoryIdList", classes = {
        @ConstructorResult(targetClass = ListProductWithCategoryDTO.class, columns = {
                @ColumnResult(name = "id", type = Long.class),
                @ColumnResult(name = "product_name", type = String.class),
                @ColumnResult(name = "price", type = BigDecimal.class),
                @ColumnResult(name = "category_name", type = String.class),
        })
})
public class Product extends  AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 2322864574973757981L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Tên sản phẩm là bắt buộc")
    @Size(max = 255, message = "Tên sản phẩm không được vượt quá 255 ký tự")
    @Column(name = "name")
    private String name;

    @NotNull(message = "Giá sản phẩm là bắt buộc")
    @DecimalMin(value = "0.0", inclusive = false, message = "Giá phải lớn hơn 0")
    @Column(name = "price")
    private BigDecimal price;

    @NotNull(message = "Danh mục sản phẩm là bắt buộc")
    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "content")
    private String content;

    @Column(name = "memo")
    private String memo;

}
