package org.example.java_training.controller;




import lombok.RequiredArgsConstructor;
import org.example.java_training.dto.ListElementProductDTO;
import org.example.java_training.responses.ProductListResponse;
import org.example.java_training.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.example.java_training.exceptions.NotFoundException;
import org.example.java_training.exceptions.HttpNotFoundException;
import org.example.java_training.exceptions.BadRequestException;
import org.example.java_training.exceptions.HttpBadRequestException;

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

}
