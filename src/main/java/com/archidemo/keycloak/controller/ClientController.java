package com.archidemo.keycloak.controller;

import com.archidemo.keycloak.dto.request.ClientRequest;
import com.archidemo.keycloak.dto.response.ClientResponse;
import com.archidemo.keycloak.service.ClientService;
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
@RequestMapping("/api/realms/{realmName}/clients")
@Tag(name = "Clients OAuth2", description = "Gestion des Clients OAuth2 d'un Realm")
@SecurityRequirement(name = "bearerAuth")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @GetMapping
    @Operation(summary = "Lister les clients d'un realm")
    public ResponseEntity<List<ClientResponse>> getAll(@PathVariable String realmName) {
        return ResponseEntity.ok(clientService.findAll(realmName));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un client par son id interne Keycloak")
    public ResponseEntity<ClientResponse> getById(
            @PathVariable String realmName,
            @PathVariable String id) {
        return ResponseEntity.ok(clientService.findById(realmName, id));
    }

    @PostMapping
    @Operation(summary = "Créer un client")
    public ResponseEntity<ClientResponse> create(
            @PathVariable String realmName,
            @Valid @RequestBody ClientRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(clientService.create(realmName, request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour un client")
    public ResponseEntity<ClientResponse> update(
            @PathVariable String realmName,
            @PathVariable String id,
            @Valid @RequestBody ClientRequest request) {
        return ResponseEntity.ok(clientService.update(realmName, id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un client")
    public ResponseEntity<Void> delete(
            @PathVariable String realmName,
            @PathVariable String id) {
        clientService.delete(realmName, id);
        return ResponseEntity.noContent().build();
    }
}
