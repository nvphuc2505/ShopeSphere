package com.shop_sphere.product_service.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
public class SecurityConfig {

    // realm_access.roles (keycloak) -> Realm roles
    private static final String REALM_LEVEL_ROLE_CLAIM = "realm_access";
    private static final String REALM_LEVEL_ROLES = "roles";

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
            .authorizeHttpRequests(
            author -> {
                author.requestMatchers("/storefront/**").permitAll();
                author.requestMatchers("/backoffice/**").hasRole("admin");
                author.anyRequest().authenticated();
            })
            .oauth2ResourceServer(
                oauth2 ->
                    oauth2.jwt(
                        jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverterCustom())
                    )
            );

        return httpSecurity.build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverterCustom() {

        /*
            -- Sample of realm_access extract from JWT (Token)
                "realm_access": {
                    "roles": [
                        "offline_access",
                        "admin",
                        "default-roles-shopsphere",
                        "uma_authorization",
                        "customer"
                    ]
                }
        */

        // Convert JWT to List of GrantedAuthority
        Converter<Jwt, Collection<GrantedAuthority>> jwtCollectionConverter =
            jwt -> {
                Map<String, Collection<String>> realmAccess = jwt.getClaim(REALM_LEVEL_ROLE_CLAIM);
                Collection<String> roles = realmAccess.get(REALM_LEVEL_ROLES);
                return roles.stream()
                        .map(role -> new SimpleGrantedAuthority(role))
                        .collect(Collectors.toList());
            };

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtCollectionConverter);

        return jwtAuthenticationConverter;
    }

}
