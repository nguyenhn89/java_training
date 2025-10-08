package org.example.java_training.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.java_training.dto.ListElementProductDTO;
import org.example.java_training.repository.ProductRepositoryCustom;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepositoryCustom productRepository;

    public List<ListElementProductDTO> getListProduct() {

        return productRepository.getListProduct();
    }
}
