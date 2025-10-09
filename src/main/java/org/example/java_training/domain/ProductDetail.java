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
@Table(name = "product_detail")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetail extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 2322864574973757981L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "description", length = 2000)
    private String description;

    @Column(name = "specification", length = 2000)
    private String specification;
}
