package org.example.java_training.service;


import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.GetResponse;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.java_training.builder.GenericBuilder;
import org.example.java_training.domain.Category;
import org.example.java_training.domain.Product;
import org.example.java_training.domain.ProductDocument;
import org.example.java_training.dto.CategoryCountDTO;
import org.example.java_training.dto.ListElementProductDTO;
import org.example.java_training.dto.ListProductWithCategoryDTO;
import org.example.java_training.repository.CategoryRepository;
import org.example.java_training.repository.ProductRepository;
//import org.example.java_training.responses.ProductSearchRepository;
import org.example.java_training.specification.ProductSpecification;
import org.springframework.data.domain.PageImpl;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.example.java_training.request.ProductCreateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
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

//    private final ProductSearchRepository productSearchRepository;

    private final ElasticsearchClient client;

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
    public Page<ListProductWithCategoryDTO> searchProductsCriteriaApi(String name, Long categoryId, Double minPrice, Double maxPrice, Pageable pageable) {
        return productRepository.searchProductsCriteriaApi(name, categoryId, minPrice, maxPrice, pageable);
    }

    public List<CategoryCountDTO> countProductsByCategory() {
        return productRepository.countProductsByCategory();
    }

    public List<Product> findExpensiveProducts() {
        return productRepository.findExpensiveProducts();
    }

    //search use JpaSpecificationExecutor
    public Page<ListProductWithCategoryDTO> searchProductsSpecificationExecutor(String name, Long categoryId, BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable) {

        var spec = ProductSpecification.filter(name, categoryId, minPrice, maxPrice);

        Page<Product> productPage = productRepository.findAll(spec, pageable);

        if (productPage.isEmpty()) {
            return new PageImpl<>(Collections.emptyList(), pageable, 0L);
        }

        Set<Long> categoryIds = productPage.getContent()
                .stream()
                .map(Product::getCategoryId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

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


    @Scheduled(fixedRate = 3 * 60 * 1000)
    public String reindexAllProducts() throws Exception {
        List<Product> products = productRepository.findAll();

        int successCount = 0;

        for (Product p : products) {
            ProductDocument doc = new ProductDocument(
                    p.getId(),
                    p.getName(),
                    p.getPrice(),
                    p.getCategoryId(),
                    p.getContent(),
                    p.getMemo()
            );

            client.index(i -> i
                    .index("products")      // tên index
                    .id(doc.getId().toString()) // id document
                    .document(doc)
            );
            successCount++;
        }

        return "✅ Reindex success: " + successCount + " products indexed to Elasticsearch.";
    }

    public ProductDocument findById(String id) throws Exception {
        GetResponse<ProductDocument> response = client.get(g -> g
                        .index("products")
                        .id(id),
                ProductDocument.class
        );
        return response.found() ? response.source() : null;
    }

    // Search bằng multi_match
    public List<ProductDocument> searchAllFields(String keyword) throws IOException {
        SearchResponse<ProductDocument> response = client.search(s -> s
                        .index("products")
                        .query(q -> q
                                .multiMatch(m -> m
                                        .query(keyword)
                                        .fields("name^2", "memo", "content") // name ưu tiên hơn
                                        .fuzziness("AUTO") // Cho phép sai chính tả nhẹ
                                )
                        ),
                ProductDocument.class
        );

        List<ProductDocument> results = response.hits().hits().stream()
                .map(Hit::source) // mỗi hit lấy ra document
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        return results;
    }

    //Gợi ý tìm kiếm (Prefix search)
    public List<String> suggestProductNames(String prefix) throws IOException {
        SearchResponse<ProductDocument> response = client.search(s -> s
                        .index("products")
                        .query(q -> q
                                .prefix(p -> p
                                        .field("name.keyword")
                                        .value(prefix.toLowerCase())
                                )
                        )
                        .size(5), // chỉ lấy top 5 gợi ý
                ProductDocument.class
        );

        return response.hits().hits().stream()
                .map(hit -> hit.source().getName())
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }


}
