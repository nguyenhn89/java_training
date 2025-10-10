package org.example.java_training.repository;
import org.example.java_training.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.math.BigDecimal;
import java.util.Map;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryCustom, JpaSpecificationExecutor<Product> {


    @Query(value = """
        SELECT p.id AS id,
               p.name AS name,
               p.price AS price,
               c.name AS categoryName
        FROM product p
        LEFT JOIN category c ON p.category_id = c.id
        WHERE (:name IS NULL OR p.name LIKE %:name%)
          AND (:categoryId IS NULL OR p.category_id = :categoryId)
          AND (:minPrice IS NULL OR p.price >= :minPrice)
          AND (:maxPrice IS NULL OR p.price <= :maxPrice)
        """,
            countQuery = """
        SELECT COUNT(*) FROM product p
        WHERE (:name IS NULL OR p.name LIKE %:name%)
          AND (:categoryId IS NULL OR p.category_id = :categoryId)
          AND (:minPrice IS NULL OR p.price >= :minPrice)
          AND (:maxPrice IS NULL OR p.price <= :maxPrice)
        """,
            nativeQuery = true)
    Page<Map<String, Object>> searchManual(
            @Param("name") String name,
            @Param("categoryId") Long categoryId,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            Pageable pageable
    );
}
