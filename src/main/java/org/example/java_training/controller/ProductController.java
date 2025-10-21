package org.example.java_training.controller;




import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.java_training.domain.Product;
import org.example.java_training.domain.ProductDocument;
import org.example.java_training.dto.CategoryCountDTO;
import org.example.java_training.dto.ListElementProductDTO;
import org.example.java_training.dto.ListProductWithCategoryDTO;
import org.example.java_training.responses.ProductListResponse;
import org.example.java_training.responses.ProductWithCategoryIdListResponse;
import org.example.java_training.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(value = "/api/products")
public class ProductController {

    private final ProductService productService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity<?> getListProduct(
    ) {
        try {
            List<ListElementProductDTO> productList = productService.getListProduct();
            return ResponseEntity.ok(new ProductListResponse(productList));
        } catch (NotFoundException e) {
            throw new HttpNotFoundException(e.getMessage(), e);
        } catch (BadRequestException e) {
            throw new HttpBadRequestException(e.getMessage(), e);
        }
    }


    @RequestMapping(value = "/", method = RequestMethod.POST)
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


    @RequestMapping(value = "/product_with_category/{categoryId}", method = RequestMethod.GET)
    @PreAuthorize("hasRole('USER')")
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
        productService.deleteProduct(id);
        return ResponseEntity.ok("Delete success");
    }


    //http://localhost:8081/api/products/advanced-search?name=product 1 update&minPrice=10&maxPrice=500&categoryId=1&page=0&size=5&sort=price,asc

    /* search pagination sort use Criteria API */
    @RequestMapping(value = "/search_criteria_api", method = RequestMethod.GET)
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


    @RequestMapping(value = "/search_jpa_specification_executor", method = RequestMethod.GET)
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

    @RequestMapping(value = "/search-manual", method = RequestMethod.GET)
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

    //electicsearch
    @GetMapping("/elasticsearch")
    public List<ProductDocument> elasticSearch(@RequestParam String q) throws IOException {
        return productService.searchAllFields(q);
    }

    @GetMapping("/elasticsearch_suggest")
    public List<String> suggestProductNames(String prefix) throws IOException
    {
        return productService.suggestProductNames(prefix);
    }
    //
    @GetMapping("/reindex")
    public ResponseEntity<String> reindex() throws Exception {
        String message = productService.reindexAllProducts();
        return ResponseEntity.ok(message);
    }

    @GetMapping("/elastic/{id}")
    public ResponseEntity<ProductDocument> getProduct(@PathVariable String id) {
        try {
            ProductDocument product = productService.findById(id);
            if (product != null) {
                return ResponseEntity.ok(product);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
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
