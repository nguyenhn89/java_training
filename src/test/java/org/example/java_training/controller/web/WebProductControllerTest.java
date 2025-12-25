package org.example.java_training.controller.web;

import org.example.java_training.dto.ListElementProductDTO;
import org.example.java_training.responses.ProductListResponse;
import org.example.java_training.service.ProductService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WebProductController.class)
@AutoConfigureMockMvc
class WebProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    @Test
    @WithMockUser(roles = {"USER"})
    void listProductsSuccess() throws Exception {

        ProductListResponse response = new ProductListResponse(
                List.of(),
                20L,
                2,
                1,
                10
        );

        Mockito.when(productService.getListProduct(1, 10))
                .thenReturn(response);

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(view().name("products/list"))
                .andExpect(model().attributeExists("products"))
                .andExpect(model().attribute("currentPage", 1))
                .andExpect(model().attribute("totalPages", 2))
                .andExpect(model().attribute("pageSize", 10))
                .andExpect(model().attributeExists("currentPageSidebar"));
    }


    @Test
    void listProductsForbiddenWhenNotLogin() throws Exception {
        mockMvc.perform(get("/products"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void createProductFormSuccess() throws Exception {
        Mockito.when(productService.getCategories())
                .thenReturn(List.of());

        mockMvc.perform(get("/products/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("products/create"))
                .andExpect(model().attributeExists("product"))
                .andExpect(model().attributeExists("categories"))
                .andExpect(model().attribute("currentPageSidebar", "product-create"));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void createProductValidationError() throws Exception {
        mockMvc.perform(post("/products/create")
                        .with(csrf())
                        .param("name", "")        // invalid
                        .param("price", "")
                        .param("content", "")
                        .param("categoryId", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("products/create"));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void createProductSuccess() throws Exception {
        Mockito.when(productService.registerProduct(Mockito.any()))
                .thenReturn(1L);

        mockMvc.perform(post("/products/create")
                        .with(csrf())
                        .param("name", "Test Product")
                        .param("price", "1000")
                        .param("content", "Test content")
                        .param("categoryId", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/products"))
                .andExpect(flash().attributeExists("message"));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void editProductFormSuccess() throws Exception {
        ListElementProductDTO dto = new ListElementProductDTO();
        dto.setId(1L);
        dto.setName("Product A");

        Mockito.when(productService.findByIdElement(1L))
                .thenReturn(dto);
        Mockito.when(productService.getCategories())
                .thenReturn(List.of());

        mockMvc.perform(get("/products/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("products/edit"))
                .andExpect(model().attributeExists("product"))
                .andExpect(model().attributeExists("categories"))
                .andExpect(model().attribute("currentPageSidebar", "product-edit"));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void updateProductSuccess() throws Exception {
        ListElementProductDTO updated = new ListElementProductDTO();
        updated.setId(1L);

        Mockito.when(productService.updateProduct(Mockito.eq(1L), Mockito.any()))
                .thenReturn(updated);

        mockMvc.perform(post("/products/update")
                        .with(csrf())
                        .param("id", "1")
                        .param("name", "Updated Product")
                        .param("price", "2000")
                        .param("content", "Updated content")
                        .param("categoryId", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/products"))
                .andExpect(flash().attributeExists("message"));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void deleteProductSuccess() throws Exception {
        mockMvc.perform(delete("/products/1")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/products"));

        Mockito.verify(productService)
                .deleteProductById(1L);
    }
}
