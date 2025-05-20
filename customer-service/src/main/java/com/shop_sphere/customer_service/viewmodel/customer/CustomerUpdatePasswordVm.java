package com.shop_sphere.customer_service.viewmodel.customer;

public record CustomerUpdatePasswordVm(
        String currentPassword,
        String newPassword,
        String confirmPassword
) {
}
