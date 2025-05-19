package com.shop_sphere.customer_service.viewmodel.address;

import jakarta.validation.constraints.NotBlank;

public record UserAddressPostVm (

        @NotBlank
        String address,

        @NotBlank
        String city,

        @NotBlank
        String state,

        @NotBlank
        String country,

        @NotBlank
        String phoneNumber

) {}