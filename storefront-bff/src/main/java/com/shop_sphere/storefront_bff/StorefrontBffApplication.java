package com.shop_sphere.storefront_bff;

import com.shop_sphere.storefront_bff.config.ServiceUrlConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;

@SpringBootApplication
@EnableWebFluxSecurity
@EnableConfigurationProperties(ServiceUrlConfig.class)
public class StorefrontBffApplication {

	public static void main(String[] args) {
		SpringApplication.run(StorefrontBffApplication.class, args);
	}

}
