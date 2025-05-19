package com.shop_sphere.customer_service.viewmodel.customer;

import java.util.List;

public record CustomerListVm(
        int totalUser,
        List<CustomerVm> customers,
        int totalPage
) {
}
