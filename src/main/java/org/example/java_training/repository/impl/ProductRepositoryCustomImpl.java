package org.example.java_training.repository.impl;

import org.example.java_training.dto.ListElementProductDTO;
import org.example.java_training.dto.ListProductWithCategory;
import org.example.java_training.repository.ProductRepositoryCustom;
import org.example.java_training.repository.RepositoryCustomUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ProductRepositoryCustomImpl extends RepositoryCustomUtils implements ProductRepositoryCustom {

    private final JpaTransactionManager transactionManager;

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
    public List<ListProductWithCategory> getProductWithCagtegoryId(Long categoryId) {

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
}
