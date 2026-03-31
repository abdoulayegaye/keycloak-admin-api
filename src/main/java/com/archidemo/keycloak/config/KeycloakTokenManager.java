package com.archidemo.keycloak.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.Map;

@Component
public class KeycloakTokenManager {

    private static final Logger log = LoggerFactory.getLogger(KeycloakTokenManager.class);

    private final RestTemplate restTemplate = new RestTemplate();
    private final KeycloakProperties properties;

    private String cachedToken;
    private Instant tokenExpiry = Instant.MIN;

    public KeycloakTokenManager(KeycloakProperties properties) {
        this.properties = properties;
    }

    public synchronized String getToken() {
        if (cachedToken == null || Instant.now().isAfter(tokenExpiry.minusSeconds(30))) {
            refreshToken();
        }
        return cachedToken;
    }

    @SuppressWarnings("unchecked")
    private void refreshToken() {
        String url = properties.getServerUrl()
                + "/realms/" + properties.getRealm()
                + "/protocol/openid-connect/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "password");
        form.add("client_id", properties.getClientId());
        form.add("username", properties.getUsername());
        form.add("password", properties.getPassword());

        Map<String, Object> body = restTemplate.postForObject(
                url, new HttpEntity<>(form, headers), Map.class);

        if (body == null || !body.containsKey("access_token")) {
            throw new IllegalStateException("Impossible d'obtenir un token admin depuis Keycloak");
        }

        cachedToken = (String) body.get("access_token");
        long expiresIn = ((Number) body.get("expires_in")).longValue();
        tokenExpiry = Instant.now().plusSeconds(expiresIn);
        log.debug("Token admin Keycloak rafraîchi, expire dans {}s", expiresIn);
    }
}
