package com.archidemo.keycloak.service;

import com.archidemo.keycloak.dto.request.RoleRequest;
import com.archidemo.keycloak.dto.response.RoleResponse;
import com.archidemo.keycloak.mapper.RoleMapper;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.RoleRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleService {

    private static final Logger log = LoggerFactory.getLogger(RoleService.class);

    @Autowired
    private Keycloak keycloak;

    // ─── Rôles du Realm ─────────────────────────────────────────────────────────

    public List<RoleResponse> findAllRealmRoles(String realmName) {
        return keycloak.realm(realmName).roles().list()
                .stream()
                .map(RoleMapper::toResponse)
                .collect(Collectors.toList());
    }

    public RoleResponse createRealmRole(String realmName, RoleRequest request) {
        keycloak.realm(realmName).roles().create(RoleMapper.toRepresentation(request));
        log.info("Rôle créé dans le realm {} : {}", realmName, request.getName());
        return RoleMapper.toResponse(keycloak.realm(realmName).roles().get(request.getName()).toRepresentation());
    }

    public void deleteRealmRole(String realmName, String roleName) {
        keycloak.realm(realmName).roles().get(roleName).remove();
        log.info("Rôle supprimé du realm {} : {}", realmName, roleName);
    }

    // ─── Assignation des rôles à un utilisateur ──────────────────────────────────

    public void assignRolesToUser(String realmName, String userId, List<RoleRequest> roles) {
        keycloak.realm(realmName).users().get(userId).roles().realmLevel().add(resolveRoles(realmName, roles));
        log.info("Rôles assignés à l'utilisateur {} dans le realm {}", userId, realmName);
    }

    public List<RoleResponse> getUserRoles(String realmName, String userId) {
        return keycloak.realm(realmName).users().get(userId).roles().realmLevel().listAll()
                .stream()
                .map(RoleMapper::toResponse)
                .collect(Collectors.toList());
    }

    public void removeRolesFromUser(String realmName, String userId, List<RoleRequest> roles) {
        keycloak.realm(realmName).users().get(userId).roles().realmLevel().remove(resolveRoles(realmName, roles));
        log.info("Rôles retirés de l'utilisateur {} dans le realm {}", userId, realmName);
    }

    // ─── Helper ─────────────────────────────────────────────────────────────────

    private List<RoleRepresentation> resolveRoles(String realmName, List<RoleRequest> roles) {
        return roles.stream()
                .map(r -> keycloak.realm(realmName).roles().get(r.getName()).toRepresentation())
                .collect(Collectors.toList());
    }
}
