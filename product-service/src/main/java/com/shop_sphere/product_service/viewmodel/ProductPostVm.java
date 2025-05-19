package com.shop_sphere.product_service.viewmodel;

import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public record ProductPostVm (

        @NotBlank String name,
        @NotBlank String color,
        @NotBlank String size,
        String description,
        @NotBlank BigDecimal price,
        @NotBlank Integer quantity           // Số lượng sản phẩm được thêm

) {
}
