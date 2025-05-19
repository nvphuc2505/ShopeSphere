package com.shop_sphere.customer_service.exception;

public class CustomerNotFoundException extends RuntimeException {
    public CustomerNotFoundException(String name, String value) {
        super(String.format("User with %s {%s} not found.", name, value));
    }
}
