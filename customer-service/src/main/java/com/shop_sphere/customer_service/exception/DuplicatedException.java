package com.shop_sphere.customer_service.exception;

public class DuplicatedException extends RuntimeException {
    public DuplicatedException(String message) {
        super(message + " of user already exists.");
    }
}
