package com.archidemo.keycloak.service;

import com.archidemo.keycloak.client.UserAdminClient;
import com.archidemo.keycloak.dto.request.CredentialRequest;
import com.archidemo.keycloak.dto.request.UserRequest;
import com.archidemo.keycloak.dto.response.UserResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserAdminClient userClient;

    public UserService(UserAdminClient userClient) {
        this.userClient = userClient;
    }

    public List<UserResponse> findAll(String realmName) {
        return userClient.findAll(realmName);
    }

    public UserResponse findById(String realmName, String userId) {
        return userClient.findById(realmName, userId);
    }

    public UserResponse create(String realmName, UserRequest request) {
        ResponseEntity<Void> response = userClient.create(realmName, request);
        String userId = extractIdFromLocation(response);
        log.info("Utilisateur créé dans le realm {} : {}", realmName, userId);
        return userClient.findById(realmName, userId);
    }

    public UserResponse update(String realmName, String userId, UserRequest request) {
        // On n'envoie pas les credentials lors d'un update pour ne pas les écraser
        Map<String, Object> body = new HashMap<>();
        body.put("username", request.getUsername());
        body.put("email", request.getEmail());
        body.put("firstName", request.getFirstName());
        body.put("lastName", request.getLastName());
        body.put("enabled", request.isEnabled());
        userClient.update(realmName, userId, body);
        log.info("Utilisateur mis à jour dans le realm {} : {}", realmName, userId);
        return userClient.findById(realmName, userId);
    }

    public void delete(String realmName, String userId) {
        userClient.delete(realmName, userId);
        log.info("Utilisateur supprimé du realm {} : {}", realmName, userId);
    }

    public void resetPassword(String realmName, String userId, CredentialRequest request) {
        userClient.resetPassword(realmName, userId, request);
        log.info("Mot de passe réinitialisé pour l'utilisateur {} dans le realm {}", userId, realmName);
    }

    private String extractIdFromLocation(ResponseEntity<Void> response) {
        String location = response.getHeaders().getFirst(HttpHeaders.LOCATION);
        if (location == null) throw new IllegalStateException("Location header absent dans la réponse Keycloak");
        return location.substring(location.lastIndexOf('/') + 1);
    }
}
