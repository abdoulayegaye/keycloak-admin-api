package com.archidemo.keycloak.mapper;

import com.archidemo.keycloak.dto.request.ClientRequest;
import com.archidemo.keycloak.dto.response.ClientResponse;
import org.keycloak.representations.idm.ClientRepresentation;

public class ClientMapper {

    public static ClientRepresentation toRepresentation(ClientRequest request) {
        ClientRepresentation c = new ClientRepresentation();
        c.setClientId(request.getClientId());
        c.setName(request.getName());
        c.setProtocol(request.getProtocol());
        c.setPublicClient(request.isPublicClient());
        c.setRedirectUris(request.getRedirectUris());
        c.setEnabled(request.isEnabled());
        return c;
    }

    public static ClientResponse toResponse(ClientRepresentation c) {
        ClientResponse dto = new ClientResponse();
        dto.setId(c.getId());
        dto.setClientId(c.getClientId());
        dto.setName(c.getName());
        dto.setProtocol(c.getProtocol());
        dto.setPublicClient(Boolean.TRUE.equals(c.isPublicClient()));
        dto.setRedirectUris(c.getRedirectUris());
        dto.setEnabled(Boolean.TRUE.equals(c.isEnabled()));
        return dto;
    }
}
