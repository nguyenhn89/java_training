package org.example.java_training.repository;

import org.example.java_training.dto.ListElementProductDTO;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface ProductRepositoryCustom {

    List<ListElementProductDTO> getListProduct();
}