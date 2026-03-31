package com.archidemo.keycloak.service;

import com.archidemo.keycloak.client.ClientAdminClient;
import com.archidemo.keycloak.dto.request.ClientRequest;
import com.archidemo.keycloak.dto.response.ClientResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientService {

    private static final Logger log = LoggerFactory.getLogger(ClientService.class);

    private final ClientAdminClient clientClient;

    public ClientService(ClientAdminClient clientClient) {
        this.clientClient = clientClient;
    }

    public List<ClientResponse> findAll(String realmName) {
        return clientClient.findAll(realmName);
    }

    public ClientResponse findById(String realmName, String id) {
        return clientClient.findById(realmName, id);
    }

    public ClientResponse create(String realmName, ClientRequest request) {
        ResponseEntity<Void> response = clientClient.create(realmName, request);
        String id = extractIdFromLocation(response);
        log.info("Client créé dans le realm {} : {}", realmName, id);
        return clientClient.findById(realmName, id);
    }

    public ClientResponse update(String realmName, String id, ClientRequest request) {
        clientClient.update(realmName, id, request);
        log.info("Client mis à jour dans le realm {} : {}", realmName, id);
        return clientClient.findById(realmName, id);
    }

    public void delete(String realmName, String id) {
        clientClient.delete(realmName, id);
        log.info("Client supprimé du realm {} : {}", realmName, id);
    }

    private String extractIdFromLocation(ResponseEntity<Void> response) {
        String location = response.getHeaders().getFirst(HttpHeaders.LOCATION);
        if (location == null) throw new IllegalStateException("Location header absent dans la réponse Keycloak");
        return location.substring(location.lastIndexOf('/') + 1);
    }
}
