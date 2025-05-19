package com.shop_sphere.product_service.exception;

public class DuplicatedException extends RuntimeException {
    public DuplicatedException(String message) {
        super(message + " of product already exists.");
    }
}
