package com.archidemo.keycloak.controller;

import com.archidemo.keycloak.dto.request.RoleRequest;
import com.archidemo.keycloak.dto.response.RoleResponse;
import com.archidemo.keycloak.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Rôles", description = "Gestion des Rôles et Assignation")
@SecurityRequirement(name = "bearerAuth")
public class RoleController {

    @Autowired
    private RoleService roleService;

    // ─── Rôles du Realm ─────────────────────────────────────────────────────────

    @GetMapping("/api/realms/{realmName}/roles")
    @Operation(summary = "Lister les rôles du realm")
    public ResponseEntity<List<RoleResponse>> getAllRealmRoles(@PathVariable String realmName) {
        return ResponseEntity.ok(roleService.findAllRealmRoles(realmName));
    }

    @PostMapping("/api/realms/{realmName}/roles")
    @Operation(summary = "Créer un rôle dans le realm")
    public ResponseEntity<RoleResponse> createRealmRole(
            @PathVariable String realmName,
            @Valid @RequestBody RoleRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(roleService.createRealmRole(realmName, request));
    }

    @DeleteMapping("/api/realms/{realmName}/roles/{roleName}")
    @Operation(summary = "Supprimer un rôle du realm")
    public ResponseEntity<Void> deleteRealmRole(
            @PathVariable String realmName,
            @PathVariable String roleName) {
        roleService.deleteRealmRole(realmName, roleName);
        return ResponseEntity.noContent().build();
    }

    // ─── Assignation des rôles ───────────────────────────────────────────────────

    @PostMapping("/api/realms/{realmName}/users/{userId}/roles")
    @Operation(summary = "Assigner des rôles à un utilisateur")
    public ResponseEntity<Void> assignRoles(
            @PathVariable String realmName,
            @PathVariable String userId,
            @RequestBody List<@Valid RoleRequest> roles) {
        roleService.assignRolesToUser(realmName, userId, roles);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/api/realms/{realmName}/users/{userId}/roles")
    @Operation(summary = "Lister les rôles d'un utilisateur")
    public ResponseEntity<List<RoleResponse>> getUserRoles(
            @PathVariable String realmName,
            @PathVariable String userId) {
        return ResponseEntity.ok(roleService.getUserRoles(realmName, userId));
    }

    @DeleteMapping("/api/realms/{realmName}/users/{userId}/roles")
    @Operation(summary = "Retirer des rôles d'un utilisateur")
    public ResponseEntity<Void> removeRoles(
            @PathVariable String realmName,
            @PathVariable String userId,
            @RequestBody List<@Valid RoleRequest> roles) {
        roleService.removeRolesFromUser(realmName, userId, roles);
        return ResponseEntity.noContent().build();
    }
}
