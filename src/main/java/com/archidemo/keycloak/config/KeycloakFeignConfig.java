package com.archidemo.keycloak.config;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;

/**
 * Configuration Feign per-client : ajoute le Bearer token admin dans chaque requête.
 * PAS de @Configuration — chargée uniquement dans le contexte Feign enfant.
 */
public class KeycloakFeignConfig {

    private final KeycloakTokenManager tokenManager;

    public KeycloakFeignConfig(KeycloakTokenManager tokenManager) {
        this.tokenManager = tokenManager;
    }

    @Bean
    public RequestInterceptor keycloakBearerInterceptor() {
        return template -> template.header("Authorization", "Bearer " + tokenManager.getToken());
    }
}
