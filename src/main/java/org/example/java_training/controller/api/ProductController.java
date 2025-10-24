package org.example.java_training.controller.api;




import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.java_training.domain.Product;
import org.example.java_training.dto.CategoryCountDTO;
import org.example.java_training.dto.ListElementProductDTO;
import org.example.java_training.dto.ListProductWithCategoryDTO;
import org.example.java_training.responses.ProductListResponse;
import org.example.java_training.responses.ProductWithCategoryIdListResponse;
import org.example.java_training.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.example.java_training.exceptions.NotFoundException;
import org.example.java_training.exceptions.HttpNotFoundException;
import org.example.java_training.exceptions.BadRequestException;
import org.example.java_training.exceptions.HttpBadRequestException;
import org.example.java_training.request.ProductCreateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(value = "/api/products")
public class ProductController {

    private final ProductService productService;

    @GetMapping("/")
    public ResponseEntity<?> getListProduct(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        try {
            ProductListResponse productList = productService.getListProduct(page, size);
            return ResponseEntity.ok(productList);
        } catch (NotFoundException e) {
            throw new HttpNotFoundException(e.getMessage(), e);
        } catch (BadRequestException e) {
            throw new HttpBadRequestException(e.getMessage(), e);
        }
    }


    @PostMapping("/")
    public ResponseEntity<?> createProduct(@Valid @RequestBody ProductCreateRequest product) {
        try {
            Long productId = productService.registerProduct(product);
            return ResponseEntity.ok(productId);
        } catch (NotFoundException e) {
            throw new HttpNotFoundException(e.getMessage(), e);
        } catch (BadRequestException e) {
            throw new HttpBadRequestException(e.getMessage(), e);
        }

    }


    @GetMapping("/product_with_category/{categoryId}")
    public ResponseEntity<?> getProductWithCagtegoryId(@PathVariable Long categoryId) {
        try {
            List<ListProductWithCategoryDTO> productWithCagtegoryIdList = productService.getProductWithCagtegoryId(categoryId);
            return ResponseEntity.ok(new ProductWithCategoryIdListResponse(productWithCagtegoryIdList));
        } catch (NotFoundException e) {
            throw new HttpNotFoundException(e.getMessage(), e);
        } catch (BadRequestException e) {
            throw new HttpBadRequestException(e.getMessage(), e);
        }
    }

    // PUT update product
    @PutMapping("/{id}")
    public ResponseEntity<?> udpate(@PathVariable("id") Long id, @RequestBody ListElementProductDTO listElementProductDTO) {
        ListElementProductDTO dto = productService.updateProduct(id, listElementProductDTO);
        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.notFound().build();
    }

    // DELETE product
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        Long deletedId = productService.deleteProductById(id);
        return ResponseEntity.ok(Map.of(
                "message", "Delete success",
                "productId", deletedId
        ));
    }

    @GetMapping( "/search_criteria_api")
    public ResponseEntity<Page<ListProductWithCategoryDTO>> search(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<ListProductWithCategoryDTO> productSearch = productService.searchProductsCriteriaApi(name, categoryId, minPrice, maxPrice, pageable);
        return ResponseEntity.ok(productSearch);
    }


    @GetMapping("/search_jpa_specification_executor")
    public ResponseEntity<Page<ListProductWithCategoryDTO>> advancedSearch(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<ListProductWithCategoryDTO> productSearch = productService.searchProductsSpecificationExecutor(name, categoryId, minPrice, maxPrice, pageable);
        return ResponseEntity.ok(productSearch);
    }

    @GetMapping("/search-manual")
    public ResponseEntity<Page<ListProductWithCategoryDTO>> manualSearch(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<ListProductWithCategoryDTO> productSearch = productService.searchManual(name, categoryId, minPrice, maxPrice, pageable);
        return ResponseEntity.ok(productSearch);
    }

    @GetMapping("/count-by-category")
    public ResponseEntity<List<CategoryCountDTO>> countProductsByCategory() {
        List<CategoryCountDTO> result = productService.countProductsByCategory();
        return ResponseEntity.ok(result);
    }

    /**
     * API: GET /api/products/expensive
     * Mục đích: Lấy danh sách các sản phẩm có giá cao hơn giá trung bình toàn bộ sản phẩm
     */
    @GetMapping("/expensive")
    public ResponseEntity<List<Product>> getExpensiveProducts() {
        List<Product> products = productService.findExpensiveProducts();

        if (products.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(products);
    }



}
