package com.archidemo.keycloak.service;

import com.archidemo.keycloak.dto.request.ClientRequest;
import com.archidemo.keycloak.dto.response.ClientResponse;
import com.archidemo.keycloak.mapper.ClientMapper;
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
public class ClientService {

    private static final Logger log = LoggerFactory.getLogger(ClientService.class);

    @Autowired
    private Keycloak keycloak;

    public List<ClientResponse> findAll(String realmName) {
        return keycloak.realm(realmName).clients().findAll()
                .stream()
                .map(ClientMapper::toResponse)
                .collect(Collectors.toList());
    }

    public ClientResponse findById(String realmName, String id) {
        return ClientMapper.toResponse(keycloak.realm(realmName).clients().get(id).toRepresentation());
    }

    public ClientResponse create(String realmName, ClientRequest request) {
        try (Response response = keycloak.realm(realmName).clients().create(ClientMapper.toRepresentation(request))) {
            String id = CreatedResponseUtil.getCreatedId(response);
            log.info("Client créé dans le realm {} : {}", realmName, id);
            return ClientMapper.toResponse(keycloak.realm(realmName).clients().get(id).toRepresentation());
        }
    }

    public ClientResponse update(String realmName, String id, ClientRequest request) {
        var existing = keycloak.realm(realmName).clients().get(id).toRepresentation();
        existing.setClientId(request.getClientId());
        existing.setName(request.getName());
        existing.setProtocol(request.getProtocol());
        existing.setPublicClient(request.isPublicClient());
        existing.setRedirectUris(request.getRedirectUris());
        existing.setEnabled(request.isEnabled());
        keycloak.realm(realmName).clients().get(id).update(existing);
        log.info("Client mis à jour dans le realm {} : {}", realmName, id);
        return ClientMapper.toResponse(keycloak.realm(realmName).clients().get(id).toRepresentation());
    }

    public void delete(String realmName, String id) {
        keycloak.realm(realmName).clients().get(id).remove();
        log.info("Client supprimé du realm {} : {}", realmName, id);
    }
}
