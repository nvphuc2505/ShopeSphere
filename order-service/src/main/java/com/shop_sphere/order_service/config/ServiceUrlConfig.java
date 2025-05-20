package com.shop_sphere.order_service.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "services")
public record ServiceUrlConfig (
        String product
) {
}
