package org.example.java_training.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.java_training.domain.Product;
import org.example.java_training.dto.ListElementProductDTO;
import org.example.java_training.dto.ListProductWithCategory;
import org.example.java_training.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.example.java_training.request.ProductCreateRequest;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<ListElementProductDTO> getListProduct() {

        return productRepository.getListProduct();
    }

    public Long registerProduct(ProductCreateRequest request) {
        log.info("Creating product: {}", request);
        Product product = new Product();
        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setCategoryId(request.getCategoryId());
        product.setContent(request.getContent());
        product.setMemo(request.getMemo());
        Product saved = productRepository.save(product);
        return saved.getId();
    }

    public List<ListProductWithCategory> getProductWithCagtegoryId(Long categoryId) {
        return productRepository.getProductWithCagtegoryId(categoryId);
    }



}
