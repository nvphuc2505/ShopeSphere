package com.shop_sphere.product_service.viewmodel;

import com.shop_sphere.product_service.model.Product;
import com.shop_sphere.product_service.model.enumeration.ProductStatus;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public record ProductVm (

        String name,
        String color,
        String size,
        String description,
        BigDecimal price,
        ProductStatus status

) {
    public static ProductVm fromProductModel(Product product) {
        return new ProductVm(
                product.getName(),
                product.getColor(),
                product.getSize(),
                product.getDescription(),
                product.getPrice(),
                product.getStatus()
        );
    }
}
