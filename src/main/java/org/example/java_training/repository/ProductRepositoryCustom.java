package org.example.java_training.repository;

import org.example.java_training.dto.ListElementProductDTO;
import org.example.java_training.dto.ListProductWithCategory;
import org.example.java_training.request.ProductCreateRequest;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface ProductRepositoryCustom {

    List<ListElementProductDTO> getListProduct();

    List<ListProductWithCategory> getProductWithCagtegoryId(Long categoryId);
}