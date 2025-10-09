package org.example.java_training.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "Product request", description = "product request")
public class ProductCreateRequest {

    @NotBlank(message = "Tên sản phẩm là bắt buộc")
    @Size(max = 255, message = "Tên sản phẩm không được vượt quá 255 ký tự")
    private String name;

    @NotNull(message = "Giá sản phẩm là bắt buộc")
    @DecimalMin(value = "0.0", inclusive = false, message = "Giá phải lớn hơn 0")
    private BigDecimal price;

    @NotNull(message = "Danh mục sản phẩm là bắt buộc")
    private Integer categoryId;

    @Size(max = 2000, message = "Nội dung không được quá 2000 ký tự")
    private String content;

    @Size(max = 500, message = "Memo không được quá 500 ký tự")
    private String memo;
}
