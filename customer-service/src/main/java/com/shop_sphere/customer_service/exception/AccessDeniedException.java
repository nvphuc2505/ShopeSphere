package com.shop_sphere.customer_service.exception;

public class AccessDeniedException extends RuntimeException {
    public AccessDeniedException(String message) {
        super(String.format("The user with {%s} is unauthenticated", message));
    }
}
