package org.example.java_training.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import jakarta.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "product_tag")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductTag extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 2322864574973757981L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "tag_id", nullable = false)
    private Long tagId;
}
