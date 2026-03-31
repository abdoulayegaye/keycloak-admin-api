package com.archidemo.keycloak.controller;

import com.archidemo.keycloak.dto.request.CredentialRequest;
import com.archidemo.keycloak.dto.request.UserRequest;
import com.archidemo.keycloak.dto.response.UserResponse;
import com.archidemo.keycloak.service.UserService;
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
@RequestMapping("/api/realms/{realmName}/users")
@Tag(name = "Utilisateurs", description = "Gestion des Utilisateurs d'un Realm")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    @Operation(summary = "Lister les utilisateurs d'un realm")
    public ResponseEntity<List<UserResponse>> getAll(@PathVariable String realmName) {
        return ResponseEntity.ok(userService.findAll(realmName));
    }

    @GetMapping("/{userId}")
    @Operation(summary = "Récupérer un utilisateur par son id")
    public ResponseEntity<UserResponse> getById(
            @PathVariable String realmName,
            @PathVariable String userId) {
        return ResponseEntity.ok(userService.findById(realmName, userId));
    }

    @PostMapping
    @Operation(summary = "Créer un utilisateur")
    public ResponseEntity<UserResponse> create(
            @PathVariable String realmName,
            @Valid @RequestBody UserRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.create(realmName, request));
    }

    @PutMapping("/{userId}")
    @Operation(summary = "Mettre à jour un utilisateur")
    public ResponseEntity<UserResponse> update(
            @PathVariable String realmName,
            @PathVariable String userId,
            @Valid @RequestBody UserRequest request) {
        return ResponseEntity.ok(userService.update(realmName, userId, request));
    }

    @DeleteMapping("/{userId}")
    @Operation(summary = "Supprimer un utilisateur")
    public ResponseEntity<Void> delete(
            @PathVariable String realmName,
            @PathVariable String userId) {
        userService.delete(realmName, userId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{userId}/resetpassword")
    @Operation(summary = "Réinitialiser le mot de passe d'un utilisateur")
    public ResponseEntity<Void> resetPassword(
            @PathVariable String realmName,
            @PathVariable String userId,
            @Valid @RequestBody CredentialRequest request) {
        userService.resetPassword(realmName, userId, request);
        return ResponseEntity.noContent().build();
    }
}
