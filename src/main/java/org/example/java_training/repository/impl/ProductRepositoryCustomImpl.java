package org.example.java_training.repository.impl;

import jakarta.persistence.*;
import org.example.java_training.domain.Category;
import org.example.java_training.domain.Product;
import org.example.java_training.dto.ListElementProductDTO;
import org.example.java_training.dto.ListProductWithCategoryDTO;
import org.example.java_training.repository.ProductRepositoryCustom;
import org.example.java_training.repository.RepositoryCustomUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ProductRepositoryCustomImpl extends RepositoryCustomUtils implements ProductRepositoryCustom {

    private final JpaTransactionManager transactionManager;

    @PersistenceContext
    private EntityManager entityManager;

    public ProductRepositoryCustomImpl(@Qualifier("mainTransactionManager")JpaTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }


    @Override
    public List<ListElementProductDTO> getListProduct() {

        try {
        String sqlBuilder = "SELECT id, name, price\n" + "FROM product";

        Map<String, Object> parameters = new HashMap<>();

        return this.getResultList(sqlBuilder, "ProductList", parameters, transactionManager);
        } catch (Exception e) {
            transactionManager.rollback(transactionManager.getTransaction(new DefaultTransactionDefinition()));
            throw e;
        }
    }

    @Override
    public List<ListProductWithCategoryDTO> getProductWithCagtegoryId(Long categoryId) {
        try {
            String sqlBuilder = "SELECT \n"
                    + "p.id AS id, \n"
                    + "p.name AS product_name, \n"
                    + "p.price AS price, \n"
                    + "category.name AS category_name \n"
                    + "FROM java_training.product AS p \n"
                    + "    INNER JOIN " + "java_training" + ".category ON p.category_id = category.id \n"
                    + "WHERE \n"
                    + "    p.category_id = :categoryId \n";

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("categoryId", categoryId);

            return this.getResultList(sqlBuilder, "ProductWithCategoryIdList", parameters, transactionManager);
        } catch (Exception e) {
            transactionManager.rollback(transactionManager.getTransaction(new DefaultTransactionDefinition()));
            throw e;
        }
    }


    @Override
    public Page<ListProductWithCategoryDTO> searchProducts(String name, Long categoryId, Double minPrice, Double maxPrice, Pageable pageable
    ) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> cq = cb.createTupleQuery();
        Root<Product> productRoot = cq.from(Product.class);
        Root<Category> categoryRoot = cq.from(Category.class);

        // Liên kết thủ công bằng điều kiện WHERE
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(productRoot.get("categoryId"), categoryRoot.get("id")));

        if (name != null && !name.isEmpty()) {
            predicates.add(cb.like(cb.lower(productRoot.get("name")), "%" + name.toLowerCase() + "%"));
        }
        if (categoryId != null) {
            predicates.add(cb.equal(productRoot.get("categoryId"), categoryId));
        }
        if (minPrice != null) {
            predicates.add(cb.greaterThanOrEqualTo(productRoot.get("price"), minPrice));
        }
        if (maxPrice != null) {
            predicates.add(cb.lessThanOrEqualTo(productRoot.get("price"), maxPrice));
        }

        cq.where(predicates.toArray(new Predicate[0]));

        // --- SELECT các field ---
        cq.multiselect(
                productRoot.get("id").alias("id"),
                productRoot.get("name").alias("name"),
                productRoot.get("price").alias("price"),
                categoryRoot.get("name").alias("categoryName")
        );

        // --- ORDER ---
        if (pageable.getSort().isSorted()) {
            List<Order> orders = new ArrayList<>();
            for (Sort.Order order : pageable.getSort()) {
                Path<?> path = order.getProperty().equalsIgnoreCase("categoryName")
                        ? categoryRoot.get("name")
                        : productRoot.get(order.getProperty());
                orders.add(order.isAscending() ? cb.asc(path) : cb.desc(path));
            }
            cq.orderBy(orders);
        }

        TypedQuery<Tuple> query = entityManager.createQuery(cq);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());
        List<Tuple> tuples = query.getResultList();

        List<ListProductWithCategoryDTO> resultList = tuples.stream()
                .map(t -> new ListProductWithCategoryDTO(
                        t.get("id", Long.class),
                        t.get("name", String.class),
                        t.get("price", BigDecimal.class),
                        t.get("categoryName", String.class)
                ))
                .toList();

        // --- COUNT query ---
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Product> countProduct = countQuery.from(Product.class);
        Root<Category> countCategory = countQuery.from(Category.class);
        List<Predicate> countPreds = new ArrayList<>();
        countPreds.add(cb.equal(countProduct.get("categoryId"), countCategory.get("id")));
        if (name != null && !name.isEmpty()) {
            countPreds.add(cb.like(cb.lower(countProduct.get("name")), "%" + name.toLowerCase() + "%"));
        }
        if (categoryId != null) {
            countPreds.add(cb.equal(countProduct.get("categoryId"), categoryId));
        }
        if (minPrice != null) {
            countPreds.add(cb.greaterThanOrEqualTo(countProduct.get("price"), minPrice));
        }
        if (maxPrice != null) {
            countPreds.add(cb.lessThanOrEqualTo(countProduct.get("price"), maxPrice));
        }

        countQuery.select(cb.count(countProduct)).where(countPreds.toArray(new Predicate[0]));
        Long total = entityManager.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(resultList, pageable, total);
    }



}
