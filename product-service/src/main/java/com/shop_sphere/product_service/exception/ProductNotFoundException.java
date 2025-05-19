package com.shop_sphere.product_service.exception;

public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException(String key, String value) {
        super(String.format("The product with %s %s was not found.", key, value));
    }

}
