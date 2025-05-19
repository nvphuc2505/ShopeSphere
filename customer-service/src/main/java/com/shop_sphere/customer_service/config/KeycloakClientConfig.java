package com.shop_sphere.customer_service.config;

import static org.keycloak.OAuth2Constants.CLIENT_CREDENTIALS;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakClientConfig {

    private final KeycloakPropsConfig keycloakPropsConfig;

    public KeycloakClientConfig(KeycloakPropsConfig keycloakPropsConfig) {
        this.keycloakPropsConfig = keycloakPropsConfig;
    }

    @Bean
    public Keycloak keycloak() {
        return KeycloakBuilder.builder()
                .grantType(CLIENT_CREDENTIALS)
                .serverUrl(keycloakPropsConfig.getAuthServerUrl())
                .realm(keycloakPropsConfig.getRealm())
                .clientId(keycloakPropsConfig.getClientId())
                .clientSecret(keycloakPropsConfig.getSecret())
                .build();

        // Using client credentials grant type (need client ID and client secret)
        // for the purpose of service-to-service communication
    }
}
