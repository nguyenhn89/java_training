package org.example.java_training.controller.web;

import org.example.java_training.responses.ProductListResponse;
import org.example.java_training.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/products")
public class WebProductController {

    @Autowired
    private ProductService productService;

    // Danh sách sản phẩm
    @GetMapping
    public String listProducts(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            Model model) {
        ProductListResponse productList = productService.getListProduct(page, size);

        model.addAttribute("products", productList.getProductsList());
        model.addAttribute("currentPage", productList.getCurrentPage());
        model.addAttribute("totalPages", productList.getTotalPages());
        model.addAttribute("totalItems", productList.getTotalItems());
        model.addAttribute("pageSize", productList.getPageSize());
        model.addAttribute("currentPageSidebar", "products");
        return "products/list";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") Long id, RedirectAttributes redirectAttributes,
                         @RequestParam(value = "page", required = false) Integer page,
                         @RequestParam(value = "size", required = false) Integer size
    ) {
        // Xóa sản phẩm
        productService.deleteProductById(id);

        // Flash message hiển thị 1 lần
        redirectAttributes.addFlashAttribute("message", "Product " + id + " deleted successfully!");

        // Giữ nguyên page & size khi redirect
        String redirectUrl = "/products";
        List<String> params = new ArrayList<>();
        if (page != null) params.add("page=" + page);
        if (size != null) params.add("size=" + size);
        if (!params.isEmpty()) redirectUrl += "?" + String.join("&", params);

        return "redirect:" + redirectUrl;
    }
}
