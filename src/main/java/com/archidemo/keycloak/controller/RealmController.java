package com.archidemo.keycloak.controller;

import com.archidemo.keycloak.dto.request.RealmRequest;
import com.archidemo.keycloak.dto.response.RealmResponse;
import com.archidemo.keycloak.service.RealmService;
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
@RequestMapping("/api/realms")
@Tag(name = "Realms", description = "Gestion des Realms Keycloak")
@SecurityRequirement(name = "bearerAuth")
public class RealmController {

    @Autowired
    private RealmService realmService;

    @GetMapping
    @Operation(summary = "Lister tous les realms")
    public ResponseEntity<List<RealmResponse>> getAll() {
        return ResponseEntity.ok(realmService.findAll());
    }

    @GetMapping("/{realmName}")
    @Operation(summary = "Récupérer un realm par nom")
    public ResponseEntity<RealmResponse> getByName(@PathVariable String realmName) {
        return ResponseEntity.ok(realmService.findByName(realmName));
    }

    @PostMapping
    @Operation(summary = "Créer un nouveau realm")
    public ResponseEntity<RealmResponse> create(@Valid @RequestBody RealmRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(realmService.create(request));
    }

    @PutMapping("/{realmName}")
    @Operation(summary = "Mettre à jour un realm")
    public ResponseEntity<RealmResponse> update(
            @PathVariable String realmName,
            @Valid @RequestBody RealmRequest request) {
        return ResponseEntity.ok(realmService.update(realmName, request));
    }

    @DeleteMapping("/{realmName}")
    @Operation(summary = "Supprimer un realm")
    public ResponseEntity<Void> delete(@PathVariable String realmName) {
        realmService.delete(realmName);
        return ResponseEntity.noContent().build();
    }
}
