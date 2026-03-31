package com.archidemo.keycloak.service;

import com.archidemo.keycloak.client.RoleAdminClient;
import com.archidemo.keycloak.dto.request.RoleRequest;
import com.archidemo.keycloak.dto.response.RoleResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

    private static final Logger log = LoggerFactory.getLogger(RoleService.class);

    private final RoleAdminClient roleClient;

    public RoleService(RoleAdminClient roleClient) {
        this.roleClient = roleClient;
    }

    // ─── Rôles du Realm ─────────────────────────────────────────────────────────

    public List<RoleResponse> findAllRealmRoles(String realmName) {
        return roleClient.findAllRealmRoles(realmName);
    }

    public RoleResponse createRealmRole(String realmName, RoleRequest request) {
        roleClient.createRealmRole(realmName, request);
        log.info("Rôle créé dans le realm {} : {}", realmName, request.getName());
        return roleClient.findByName(realmName, request.getName());
    }

    public void deleteRealmRole(String realmName, String roleName) {
        roleClient.deleteRealmRole(realmName, roleName);
        log.info("Rôle supprimé du realm {} : {}", realmName, roleName);
    }

    // ─── Assignation des rôles à un utilisateur ──────────────────────────────────

    public void assignRolesToUser(String realmName, String userId, List<RoleRequest> roles) {
        List<RoleResponse> resolved = resolveRoles(realmName, roles);
        roleClient.assignRolesToUser(realmName, userId, resolved);
        log.info("Rôles assignés à l'utilisateur {} dans le realm {}", userId, realmName);
    }

    public List<RoleResponse> getUserRoles(String realmName, String userId) {
        return roleClient.getUserRoles(realmName, userId);
    }

    public void removeRolesFromUser(String realmName, String userId, List<RoleRequest> roles) {
        List<RoleResponse> resolved = resolveRoles(realmName, roles);
        roleClient.removeRolesFromUser(realmName, userId, resolved);
        log.info("Rôles retirés de l'utilisateur {} dans le realm {}", userId, realmName);
    }

    // ─── Helper ─────────────────────────────────────────────────────────────────

    private List<RoleResponse> resolveRoles(String realmName, List<RoleRequest> roles) {
        return roles.stream()
                .map(r -> roleClient.findByName(realmName, r.getName()))
                .toList();
    }
}
