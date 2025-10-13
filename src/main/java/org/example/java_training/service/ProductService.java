package org.example.java_training.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.java_training.builder.GenericBuilder;
import org.example.java_training.domain.Category;
import org.example.java_training.domain.Product;
import org.example.java_training.dto.ListElementProductDTO;
import org.example.java_training.dto.ListProductWithCategoryDTO;
import org.example.java_training.repository.CategoryRepository;
import org.example.java_training.repository.ProductRepository;
import org.example.java_training.specification.ProductSpecification;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.example.java_training.request.ProductCreateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final GenericBuilder builder;

    private final ProductRepository productRepository;

    private final CategoryRepository categoryRepository;

    public List<ListElementProductDTO> getListProduct() {

        return productRepository.getListProduct();
    }

    public Long registerProduct(ProductCreateRequest request) {
        log.info("Creating product: {}", request);
        Product product = new Product();
        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setCategoryId(Long.valueOf(request.getCategoryId()));
        product.setContent(request.getContent());
        product.setMemo(request.getMemo());
        Product saved = productRepository.save(product);
        return saved.getId();
    }

    public List<ListProductWithCategoryDTO> getProductWithCagtegoryId(Long categoryId) {
        log.info("list product product: ");
        return productRepository.getProductWithCagtegoryId(categoryId);
    }

    public ListElementProductDTO updateProduct(Long id, ListElementProductDTO productDTO) {
        Product product = productRepository.findById(id).orElse(null);
        if (product != null) {
            builder.updateEntity(productDTO, product);
            Product updated = productRepository.save(product);
            return builder.toDto(updated, ListElementProductDTO.class).orElse(null);
        }
        return null;
    }

    //search use Criteria API
    public Page<ListProductWithCategoryDTO> searchProducts(String name, Long categoryId, Double minPrice, Double maxPrice, Pageable pageable) {
        return productRepository.searchProducts(name, categoryId, minPrice, maxPrice, pageable);
    }

    //search use JpaSpecificationExecutor
    public Page<ListProductWithCategoryDTO> search(String name, Long categoryId, BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable) {

        // 1. Build Specification (no Criteria API manual in your code)
        var spec = ProductSpecification.filter(name, categoryId, minPrice, maxPrice);

        // 2. Query products via JpaSpecificationExecutor
        Page<Product> productPage = productRepository.findAll(spec, pageable);

        if (productPage.isEmpty()) {
            return new PageImpl<>(Collections.emptyList(), pageable, 0L);
        }

        // 3. Collect category ids (filter nulls)
        Set<Long> categoryIds = productPage.getContent()
                .stream()
                .map(Product::getCategoryId)      // assumes Product.getCategoryId() returns Long
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        // 4. Load categories in one query
        final Map<Long, String> finalCategoryMap = categoryIds.isEmpty()
                ? Collections.emptyMap()
                : categoryRepository.findAllById(categoryIds)
                .stream()
                .collect(Collectors.toMap(Category::getId, Category::getName));

        List<ListProductWithCategoryDTO> dtoList = productPage.getContent()
                .stream()
                .map(p -> new ListProductWithCategoryDTO(
                        p.getId(),
                        p.getName(),
                        p.getPrice(),
                        finalCategoryMap.getOrDefault(p.getCategoryId(), "N/A")
                ))
                .collect(Collectors.toList());

        // 6. Return Page of DTOs (keep totalElements from productPage)
        return new PageImpl<>(dtoList, pageable, productPage.getTotalElements());
    }

    public Page<ListProductWithCategoryDTO> searchManual(String name, Long categoryId,
                                                   BigDecimal minPrice, BigDecimal maxPrice,
                                                   Pageable pageable) {
        Page<Map<String, Object>> result = productRepository.searchManual(name, categoryId, minPrice, maxPrice, pageable);

        List<ListProductWithCategoryDTO> dtoList = result.getContent().stream()
                .map(row -> new ListProductWithCategoryDTO(
                        ((Number) row.get("id")).longValue(),
                        (String) row.get("name"),
                        new BigDecimal(row.get("price").toString()),
                        (String) row.get("categoryName")
                ))
                .toList();

        return new PageImpl<>(dtoList, pageable, result.getTotalElements());
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

}
