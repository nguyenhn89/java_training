package org.example.java_training.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.java_training.dto.CategoryCountDTO;
import org.example.java_training.dto.ListElementProductDTO;
import org.example.java_training.dto.ListProductWithCategoryDTO;
import org.example.java_training.request.ProductCreateRequest;
import org.example.java_training.responses.ProductListResponse;
import org.example.java_training.security.JwtUtil;
import org.example.java_training.service.ProductService;
import org.example.java_training.service.impl.CustomUserDetailsService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.example.java_training.domain.Product;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
@AutoConfigureMockMvc(addFilters = false) // tắt filter security nếu không muốn test auth
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ProductService productService;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    @MockitoBean
    private JwtUtil jwtUtil;

    @Test
    @WithMockUser(username = "admin", roles = {"USER"})
    void getListProductSuccess() throws Exception {
        List<ListElementProductDTO> productList = Collections.emptyList();
        ProductListResponse response = new ProductListResponse(
                productList,
                0L,
                0,
                1,
                10
        );

        Mockito.when(productService.getListProduct(1, 10)).thenReturn(response);

        mockMvc.perform(get("/api/products/")
                        .param("page", "1")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"USER"})
    void createProductSuccess() throws Exception {
        ProductCreateRequest request = new ProductCreateRequest();
        request.setName("Test Product");
        request.setPrice(BigDecimal.valueOf(100));
        request.setCategoryId(1L);

        Mockito.when(productService.registerProduct(request)).thenReturn(1L);

        mockMvc.perform(post("/api/products/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"USER"})
    void getProductWithCategorySuccess() throws Exception {
        List<ListProductWithCategoryDTO> products = Collections.singletonList(new ListProductWithCategoryDTO());
        Mockito.when(productService.getProductWithCagtegoryId(1L)).thenReturn(products);
        mockMvc.perform(get("/api/products/product_with_category/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productsList").exists())
                .andExpect(jsonPath("$.productsList[0]").exists());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"USER"})
    void countProductsByCategorySuccess() throws Exception {
        List<CategoryCountDTO> result = Collections.singletonList(new CategoryCountDTO(1L, 5L));
        Mockito.when(productService.countProductsByCategory()).thenReturn(result);

        mockMvc.perform(get("/api/products/count-by-category"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(result)));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"USER"})
    void searchCriteriaApiSuccess() throws Exception {
        ListProductWithCategoryDTO dto = new ListProductWithCategoryDTO();
        dto.setId(1L);
        dto.setProductName("Test Product");
        dto.setPrice(BigDecimal.valueOf(100));
        dto.setCategoryName("Category 1");

        Page<ListProductWithCategoryDTO> page = new PageImpl<>(
                List.of(dto),
                PageRequest.of(0, 10),
                1
        );

        Mockito.when(productService.searchProductsCriteriaApi(
                Mockito.any(),
                Mockito.any(),
                Mockito.any(),
                Mockito.any(),
                Mockito.<Pageable>any()
        )).thenReturn(page);

        mockMvc.perform(get("/api/products/search_criteria_api")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].product_name").value("Test Product")) // sửa lại
                .andExpect(jsonPath("$.content[0].price").value(100))
                .andExpect(jsonPath("$.content[0].category_name").value("Category 1"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"USER"})
    void updateProductSuccess() throws Exception {
        Long productId = 1L;
        ListElementProductDTO requestDto = new ListElementProductDTO();
        requestDto.setId(productId);
        requestDto.setName("Updated Product");
        requestDto.setPrice(BigDecimal.valueOf(200));
        requestDto.setCategoryId(2L);

        Mockito.when(productService.updateProduct(productId, requestDto)).thenReturn(requestDto);

        mockMvc.perform(put("/api/products/{id}", productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(productId))
                .andExpect(jsonPath("$.name").value("Updated Product")) // sửa từ productName -> name
                .andExpect(jsonPath("$.price").value(200))
                .andExpect(jsonPath("$.category_id").value(2));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"USER"})
    void updateProductNotFound() throws Exception {
        Long productId = 999L;
        ListElementProductDTO requestDto = new ListElementProductDTO();
        Mockito.when(productService.updateProduct(productId, requestDto)).thenReturn(null);

        mockMvc.perform(put("/api/products/{id}", productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"USER"})
    void deleteProductSuccess() throws Exception {
        Long productId = 1L;
        Mockito.when(productService.deleteProductById(productId)).thenReturn(productId);

        mockMvc.perform(delete("/api/products/{id}", productId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Delete success"))
                .andExpect(jsonPath("$.productId").value(productId));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"USER"})
    void advancedSearchSuccess() throws Exception {
        ListProductWithCategoryDTO dto = new ListProductWithCategoryDTO();
        dto.setId(1L);
        dto.setProductName("Advanced Product");
        dto.setPrice(BigDecimal.valueOf(150));
        dto.setCategoryName("Category A");

        Page<ListProductWithCategoryDTO> page = new PageImpl<>(List.of(dto), PageRequest.of(0, 10), 1);

        Mockito.when(productService.searchProductsSpecificationExecutor(
                Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.<Pageable>any()
        )).thenReturn(page);

        mockMvc.perform(get("/api/products/search_jpa_specification_executor")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].product_name").value("Advanced Product"))
                .andExpect(jsonPath("$.content[0].price").value(150))
                .andExpect(jsonPath("$.content[0].category_name").value("Category A"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"USER"})
    void manualSearchSuccess() throws Exception {
        ListProductWithCategoryDTO dto = new ListProductWithCategoryDTO();
        dto.setId(1L);
        dto.setProductName("Manual Product");
        dto.setPrice(BigDecimal.valueOf(180));
        dto.setCategoryName("Category B");

        Page<ListProductWithCategoryDTO> page = new PageImpl<>(List.of(dto), PageRequest.of(0, 10), 1);

        Mockito.when(productService.searchManual(
                Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.<Pageable>any()
        )).thenReturn(page);

        mockMvc.perform(get("/api/products/search-manual")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].product_name").value("Manual Product"))
                .andExpect(jsonPath("$.content[0].price").value(180))
                .andExpect(jsonPath("$.content[0].category_name").value("Category B"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"USER"})
    void getExpensiveProductsSuccess() throws Exception {
        Product product = new Product();
        product.setId(1L);
        product.setName("Expensive Product");
        product.setPrice(BigDecimal.valueOf(500));

        List<Product> products = List.of(product);

        Mockito.when(productService.findExpensiveProducts()).thenReturn(products);

        mockMvc.perform(get("/api/products/expensive"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Expensive Product"))
                .andExpect(jsonPath("$[0].price").value(500));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"USER"})
    void getExpensiveProductsNoContent() throws Exception {
        Mockito.when(productService.findExpensiveProducts()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/products/expensive"))
                .andExpect(status().isNoContent());
    }
}
