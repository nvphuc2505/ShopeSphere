package com.shop_sphere.product_service.viewmodel;

import java.math.BigDecimal;

public record ProductUpdateVm(

        BigDecimal price,
        Integer quantity,           // Số lượng sản phẩm còn lại
        Integer sold                // Số lượng sản phẩm đã bán

) {
}
