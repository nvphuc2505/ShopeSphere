package com.shop_sphere.storefront_bff.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.oauth2.client.oidc.web.server.logout.OidcClientInitiatedServerLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.logout.ServerLogoutSuccessHandler;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    private static final String REALM_ACCESS_CLAIM = "realm_access";
    private static final String ROLE_CLAIM = "roles";

    private final ReactiveClientRegistrationRepository reactiveClientRegistrationRepository;

    public SecurityConfig(ReactiveClientRegistrationRepository reactiveClientRegistrationRepository) {
        this.reactiveClientRegistrationRepository = reactiveClientRegistrationRepository;
    }



    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity serverHttpSecurity) {

        serverHttpSecurity
            .authorizeExchange(
                // Endpoint for Storefront
                authorizeExchangeSpec -> {
                    authorizeExchangeSpec.pathMatchers("/user-address/**").authenticated();
                    authorizeExchangeSpec.pathMatchers("/storefront/profile").authenticated();
                    authorizeExchangeSpec.anyExchange().permitAll();
                }
            )
            // If user unauthenticated, redirect to OAuth2 server(Google, Keycloak)
            .oauth2Login(Customizer.withDefaults())
            .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
            .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
            .csrf(ServerHttpSecurity.CsrfSpec::disable)
            .logout(
                logoutSpec ->
                    logoutSpec.logoutSuccessHandler(oidcLogoutSuccessHandler())
            );

        return serverHttpSecurity.build();
    }

    private ServerLogoutSuccessHandler oidcLogoutSuccessHandler() {
        /*
            OidcClientInitiatedServerLogoutSuccessHandler Constructor
            -- OidcClientInitiatedServerLogoutSuccessHandler(ReactiveClientRegistrationRepository clientRegistrationRepository)
         */

        OidcClientInitiatedServerLogoutSuccessHandler oidcClientInitiatedServerLogoutSuccessHandler
                = new OidcClientInitiatedServerLogoutSuccessHandler(this.reactiveClientRegistrationRepository);

        /*
            setPostLogoutRedirectUri
            -- Set the post logout redirect uri template.
            -- The supported uri template variables are: {baseScheme}, {baseHost}, {basePort} and {basePath}.
         */
        oidcClientInitiatedServerLogoutSuccessHandler.setPostLogoutRedirectUri("{baseUrl}");

        return oidcClientInitiatedServerLogoutSuccessHandler;
    }



    // This method extracts user roles for Authentication
    @Bean
    public GrantedAuthoritiesMapper userAuthoritiesMapperForKeycloak() {

        return authorities -> {
            /*
                Check Authority type: OAuth2 or OIDC (protocol)
                    OIDC -> provides ID TOKEN (Authorization)
                    OAuth2 -> provides ACCESS TOKEN (Authentication)
                Both ID TOKEN and ACCESS TOKEN is formatted by JWT (DATA FORMAT)
             */

            Set<GrantedAuthority> mappedAuthorities = new HashSet<>();
            var authority = authorities.iterator().next();
            boolean isOidc = authority instanceof OidcUserAuthority;

            if (isOidc) {
                OidcUserAuthority oidcUserAuthority = (OidcUserAuthority) authority;
                OidcUserInfo userInfo = oidcUserAuthority.getUserInfo();

                if (userInfo.hasClaim(REALM_ACCESS_CLAIM)) {
                    Map<String, Object> realmAccess = (Map<String, Object>) userInfo.getClaimAsMap(REALM_ACCESS_CLAIM);
                    Collection<String> roles = (Collection<String>) realmAccess.get(ROLE_CLAIM);
                    mappedAuthorities.addAll(generateAuthoritiesFromClaim(roles));
                }
            } else {
                OAuth2UserAuthority oauth2UserAuthority = (OAuth2UserAuthority) authority;
                Map<String, Object> userAttributes = oauth2UserAuthority.getAttributes();

                if (userAttributes.containsKey(REALM_ACCESS_CLAIM)) {
                    Map<String, Object> realmAccess = (Map<String, Object>) userAttributes.get(REALM_ACCESS_CLAIM);
                    Collection<String> roles = (Collection<String>) realmAccess.get(ROLE_CLAIM);
                    mappedAuthorities.addAll(generateAuthoritiesFromClaim(roles));
                }
            }

            return mappedAuthorities;
        };

    }

    private Collection<GrantedAuthority> generateAuthoritiesFromClaim(Collection<String> roles) {
        // SimpleGrantedAuthority
        // -- Stores a String representation of an authority granted to the Authentication object
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role))
                .collect(Collectors.toList());
    }

}
