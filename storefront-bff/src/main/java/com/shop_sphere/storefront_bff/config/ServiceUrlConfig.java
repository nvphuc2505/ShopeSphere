package com.shop_sphere.storefront_bff.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@ConfigurationProperties(prefix = "service-url")
public record ServiceUrlConfig (
        Map<String, String> service
) {
}
