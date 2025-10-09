package org.example.java_training.controller;




import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.java_training.dto.ListElementProductDTO;
import org.example.java_training.dto.ListProductWithCategory;
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
import org.example.java_training.domain.Product;
import org.example.java_training.request.ProductCreateRequest;

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
    public ResponseEntity<?> getProductWithCagtegoryId(@PathVariable Long categoryId) {
        try {
            List<ListProductWithCategory> productWithCagtegoryIdList = productService.getProductWithCagtegoryId(categoryId);
            return ResponseEntity.ok(new ProductWithCategoryIdListResponse(productWithCagtegoryIdList));
        } catch (NotFoundException e) {
            throw new HttpNotFoundException(e.getMessage(), e);
        } catch (BadRequestException e) {
            throw new HttpBadRequestException(e.getMessage(), e);
        }
    }

}
