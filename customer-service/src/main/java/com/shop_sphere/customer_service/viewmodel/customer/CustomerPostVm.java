package com.shop_sphere.customer_service.viewmodel.customer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CustomerPostVm (
        @NotBlank
        String username,

        @NotBlank
        String password,

        @NotBlank
        @Email
        String email,

        @NotBlank
        String firstName,

        @NotBlank
        String lastName,

        @NotBlank
        String role
)
{ }
