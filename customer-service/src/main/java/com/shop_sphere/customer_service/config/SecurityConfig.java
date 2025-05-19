package com.shop_sphere.customer_service.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

@Slf4j
@Configuration
public class SecurityConfig {

    /*
        log.trace("Chi tiết mức thấp nhất");
        log.debug("Thông tin debug");
        log.info("Thông tin chung");
        log.warn("Cảnh báo");
        log.error("Lỗi");
        log.fatal("Lỗi nghiêm trọng");
     */

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.authorizeHttpRequests(
            auth -> {
                // NOTE the role mapping is done using the SimpleAuthorityMapper
                auth.requestMatchers("/storefront/customer/**").hasRole("customer");
                auth.requestMatchers("/storefront/profile").hasRole("customer");
                auth.requestMatchers("/storefront/**").permitAll();
                auth.requestMatchers("/backoffice/customers/**").hasRole("admin");
                auth.anyRequest().authenticated();
            }
        ).oauth2ResourceServer(
                // Set up JWT (data-format) with Customizer.withDefaults()
                // Spring will automatically use configuration in .properties/.yml/.yaml
            oauth2 ->
                oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
        );

        return httpSecurity.build();
    }   

    // realm_access.roles -> roles
    @Bean
	public JwtAuthenticationConverter jwtAuthenticationConverter() {
		return new JwtAuthenticationConverter() {
            {
                setJwtGrantedAuthoritiesConverter(jwt -> {

                    log.info("JWT claims: {}", jwt.getClaims());
                    // System.out.println("JWT claims: " + jwt.getClaims());

                    Collection<GrantedAuthority> authorities = new ArrayList<>();

                    Map<String, Object> realmAccess = jwt.getClaim("realm_access");
                    if (realmAccess != null && realmAccess.get("roles") instanceof List<?>) {
                        List<?> roles = (List<?>) realmAccess.get("roles");
                        for (Object role : roles) {
                            if (role instanceof String roleStr) {
                                authorities.add(new SimpleGrantedAuthority("ROLE_" + roleStr));
                            }
                        }
                    }

                    log.info("Extracted authorities: {}", authorities);
                    //System.out.println("Extracted authorities: " + authorities);

                    return authorities;
                });
            }
        };
	}

}
