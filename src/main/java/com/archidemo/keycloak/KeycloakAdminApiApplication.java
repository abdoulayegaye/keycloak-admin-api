package com.archidemo.keycloak;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@ConfigurationPropertiesScan
@EnableFeignClients(basePackages = "com.archidemo.keycloak.client")
public class KeycloakAdminApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(KeycloakAdminApiApplication.class, args);
    }
}
