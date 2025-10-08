package org.example.java_training.repository.impl;

import org.example.java_training.dto.ListElementProductDTO;
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

        // Bắt đầu transaction custom
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());

        try {
        String sqlBuilder = "SELECT id, name, price\n" + "FROM product";

        Map<String, Object> parameters = new HashMap<>();

        return this.getResultList(sqlBuilder, "ProductList", parameters, transactionManager);
        } catch (Exception e) {
            transactionManager.rollback(status);
            throw e;
        }
    }

}
