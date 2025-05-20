package com.shop_sphere.customer_service.exception;

public class PasswordMismatchException extends RuntimeException {
    public PasswordMismatchException() {
        super("Passwords do not match.");
    }
}
