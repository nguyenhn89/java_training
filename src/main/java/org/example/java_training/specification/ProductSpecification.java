package org.example.java_training.specification;

import org.example.java_training.domain.Product;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class ProductSpecification {

    public static Specification<Product> filter(String name, Long categoryId, BigDecimal minPrice, BigDecimal maxPrice) {
        return (root, query, cb) -> {
            var predicates = cb.conjunction();

            if (name != null && !name.isEmpty()) {
                predicates = cb.and(predicates,
                        cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
            }

            if (categoryId != null) {
                predicates = cb.and(predicates,
                        cb.equal(root.get("categoryId"), categoryId));
            }

            if (minPrice != null) {
                predicates = cb.and(predicates,
                        cb.greaterThanOrEqualTo(root.get("price"), minPrice));
            }

            if (maxPrice != null) {
                predicates = cb.and(predicates,
                        cb.lessThanOrEqualTo(root.get("price"), maxPrice));
            }

            return predicates;
        };
    }
}
