package com.shop_sphere.product_service.viewmodel;

import java.math.BigDecimal;

public record ProductListWithCategoryVm(
        String productName,
        BigDecimal productPrice
) {
}
