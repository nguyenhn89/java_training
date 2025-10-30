package org.example.java_training.controller.web;

import jakarta.validation.Valid;
import org.example.java_training.domain.Product;
import org.example.java_training.domain.ProductDocument;
import org.example.java_training.dto.ListElementProductDTO;
import org.example.java_training.request.ProductCreateRequest;
import org.example.java_training.responses.ProductListResponse;
import org.example.java_training.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


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

    @GetMapping("/create")
    public String createProductForm(Model model) {
        model.addAttribute("product", new ListElementProductDTO());
        model.addAttribute("categories", productService.getCategories());
        System.out.println(productService.getCategories());
        return "products/create";
    }


    @PostMapping("/create")
    public String createProduct(
            @Valid @ModelAttribute("product") ListElementProductDTO productDTO,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model) {

        if (bindingResult.hasErrors()) {
            return "products/create";
        }

        try {
            ProductCreateRequest request = new ProductCreateRequest();
            request.setName(productDTO.getName());
            request.setPrice(productDTO.getPrice());
            request.setContent(productDTO.getContent());
            request.setCategoryId(productDTO.getCategoryId());

            Long productId = productService.registerProduct(request);

            redirectAttributes.addFlashAttribute("message", "Product created successfully! (ID: " + productId + ")");
            return "redirect:/products";
        } catch (Exception e) {
            model.addAttribute("error", "Error creating product: " + e.getMessage());
            return "products/create";
        }
    }

    @GetMapping("/edit/{id}")
    public String editProductForm(@PathVariable Long id, Model model) throws Exception {
        ListElementProductDTO product = productService.findByIdElement(id);
        model.addAttribute("categories", productService.getCategories());
        model.addAttribute("product", product);
        return "products/edit";
    }

    @PostMapping("/update")
    public String updateProduct(
            @Valid @ModelAttribute("product") ListElementProductDTO productDTO,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes, Model model) {
            if (bindingResult.hasErrors()) {
                model.addAttribute("error", "Please correct the errors below!");
                return "products/edit";
            }

            try {
                ListElementProductDTO updated = productService.updateProduct(productDTO.getId(), productDTO);
                if (updated != null) {
                    redirectAttributes.addFlashAttribute("message", "Product updated successfully!");
                    return "redirect:/products";
                } else {
                    model.addAttribute("error", "Product not found!");
                    return "products/edit";
                }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", " Error updating product: " + e.getMessage());
        }

        return "redirect:/products";
    }


    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") Long id, RedirectAttributes redirectAttributes,
                         @RequestParam(value = "page", required = false) Integer page,
                         @RequestParam(value = "size", required = false) Integer size
    ) {
        productService.deleteProductById(id);
        redirectAttributes.addFlashAttribute("message", "Product " + id + " deleted successfully!");

        String redirectUrl = "/products";
        List<String> params = new ArrayList<>();
        if (page != null) params.add("page=" + page);
        if (size != null) params.add("size=" + size);
        if (!params.isEmpty()) redirectUrl += "?" + String.join("&", params);

        return "redirect:" + redirectUrl;
    }
}
