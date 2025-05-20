package com.shop_sphere.customer_service.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Setter
@Getter
@Configuration
@ConfigurationProperties(value = "keycloak")
public class KeycloakPropsConfig {

    private String authServerUrl;
    private String realm;
    private String clientId;
    private String secret;

}
