package com.shop_sphere.product_service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shop_sphere.product_service.controller.ProductController;
import com.shop_sphere.product_service.model.enumeration.ProductStatus;
import com.shop_sphere.product_service.repository.ProductRepository;
import com.shop_sphere.product_service.service.ProductService;
import com.shop_sphere.product_service.viewmodel.ProductListWithCategoryVm;
import com.shop_sphere.product_service.viewmodel.ProductPostVm;
import com.shop_sphere.product_service.viewmodel.ProductVm;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.List;

// Use @MockitoBean for Unit Testing
@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @MockitoBean
    ProductRepository productRepository;

    @MockitoBean
    ProductService productService;

    @Autowired
    MockMvc mockMvc;

    @Test
    void testGetProductListByCategoryId() throws Exception {
        // Given
        Long categoryId = 1L;

        List<ProductListWithCategoryVm> results = List.of(
                new ProductListWithCategoryVm("Áo phông trơn nam", BigDecimal.valueOf(99000)),
                new ProductListWithCategoryVm("Quần ống rộng", BigDecimal.valueOf(199000))
        );

        // When & Then
        mockMvc.perform(get("/storefront/products/category/id/{id}", categoryId)
                        .with(user("admin").roles("ADMIN"))
                        .with(user("customer").roles("CUSTOMER")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].productName").value("Áo phông trơn nam"))
                .andExpect(jsonPath("$[0].productPrice").value(BigDecimal.valueOf(99000)))
                .andExpect(jsonPath("$[1].productName").value("Quần ống rộng"))
                .andExpect(jsonPath("$[1].productPrice").value(BigDecimal.valueOf(199000)));
    }

    @Test
    void testPostNewProduct() throws Exception {
        // Given
        var requestProduct = new ProductPostVm(
                "Áo phông trơn nam",
                "Xanh lam",
                "L",
                "Áo phông trơn nam logo thêu chất cotton , chuẩn form, trẻ trung, thanh lịch",
                BigDecimal.valueOf(99000),
                190
        );

        var expectedProductResponse = new ProductVm(
                "Áo phông trơn nam",
                "Xanh lam",
                "L",
                "Áo phông trơn nam logo thêu chất cotton , chuẩn form, trẻ trung, thanh lịch",
                BigDecimal.valueOf(99000),
                ProductStatus.IN_STOCK
        );

        Mockito.when(productService.createNewProduct(Mockito.any(ProductPostVm.class)))
                .thenReturn(expectedProductResponse);

        // When & Then
        mockMvc
            .perform(
                post("/backoffice/products")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsString(requestProduct))
                    .with(csrf())
                    .with(user("admin").roles("ADMIN")))    // Simulate user login
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name").value("Áo phông trơn nam"))
            .andExpect(jsonPath("$.color").value("Xanh lam"))
            .andExpect(jsonPath("$.size").value("L"))
            .andExpect(jsonPath("$.description").value("Áo phông trơn nam logo thêu chất cotton , chuẩn form, trẻ trung, thanh lịch"))
            .andExpect(jsonPath("$.price").value(99000))
            .andExpect(jsonPath("$.status").value("IN_STOCK"));

    }

}
