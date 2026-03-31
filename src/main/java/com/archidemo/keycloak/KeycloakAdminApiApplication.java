package com.archidemo.keycloak;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class KeycloakAdminApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(KeycloakAdminApiApplication.class, args);
    }
}
