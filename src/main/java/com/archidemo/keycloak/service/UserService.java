package com.archidemo.keycloak.service;

import com.archidemo.keycloak.dto.request.CredentialRequest;
import com.archidemo.keycloak.dto.request.UserRequest;
import com.archidemo.keycloak.dto.response.UserResponse;
import com.archidemo.keycloak.mapper.UserMapper;
import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private Keycloak keycloak;

    public List<UserResponse> findAll(String realmName) {
        return keycloak.realm(realmName).users().list()
                .stream()
                .map(UserMapper::toResponse)
                .collect(Collectors.toList());
    }

    public UserResponse findById(String realmName, String userId) {
        return UserMapper.toResponse(keycloak.realm(realmName).users().get(userId).toRepresentation());
    }

    public UserResponse create(String realmName, UserRequest request) {
        try (Response response = keycloak.realm(realmName).users().create(UserMapper.toRepresentation(request))) {
            String userId = CreatedResponseUtil.getCreatedId(response);
            log.info("Utilisateur créé dans le realm {} : {}", realmName, userId);
            return UserMapper.toResponse(keycloak.realm(realmName).users().get(userId).toRepresentation());
        }
    }

    public UserResponse update(String realmName, String userId, UserRequest request) {
        var existing = keycloak.realm(realmName).users().get(userId).toRepresentation();
        existing.setUsername(request.getUsername());
        existing.setEmail(request.getEmail());
        existing.setFirstName(request.getFirstName());
        existing.setLastName(request.getLastName());
        existing.setEnabled(request.isEnabled());
        keycloak.realm(realmName).users().get(userId).update(existing);
        log.info("Utilisateur mis à jour dans le realm {} : {}", realmName, userId);
        return UserMapper.toResponse(keycloak.realm(realmName).users().get(userId).toRepresentation());
    }

    public void delete(String realmName, String userId) {
        keycloak.realm(realmName).users().get(userId).remove();
        log.info("Utilisateur supprimé du realm {} : {}", realmName, userId);
    }

    public void resetPassword(String realmName, String userId, CredentialRequest request) {
        keycloak.realm(realmName).users().get(userId).resetPassword(UserMapper.toCredential(request));
        log.info("Mot de passe réinitialisé pour l'utilisateur {} dans le realm {}", userId, realmName);
    }
}
