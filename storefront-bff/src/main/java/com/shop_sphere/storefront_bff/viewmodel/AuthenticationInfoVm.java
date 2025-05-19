package com.shop_sphere.storefront_bff.viewmodel;

public record AuthenticationInfoVm (
        boolean isAuthenticated,
        String username
) {
}
