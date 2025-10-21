package org.example.java_training.repository;

import org.example.java_training.domain.Product;
import org.example.java_training.dto.CategoryCountDTO;
import org.example.java_training.dto.ListElementProductDTO;
import org.example.java_training.dto.ListProductWithCategoryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface ProductRepositoryCustom {

    List<ListElementProductDTO> getListProduct();

    List<ListProductWithCategoryDTO> getProductWithCagtegoryId(Long categoryId);

    Page<ListProductWithCategoryDTO> searchProductsCriteriaApi(String name, Long categoryId, Double minPrice, Double maxPrice, Pageable pageable);

//    Page<ListProductWithCategoryDTO> search(String name, Long categoryId, BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);

    List<CategoryCountDTO> countProductsByCategory();

    List<Product> findExpensiveProducts();
}