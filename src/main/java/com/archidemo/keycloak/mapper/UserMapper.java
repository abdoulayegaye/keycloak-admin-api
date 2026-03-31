package com.archidemo.keycloak.mapper;

import com.archidemo.keycloak.dto.request.CredentialRequest;
import com.archidemo.keycloak.dto.request.UserRequest;
import com.archidemo.keycloak.dto.response.UserResponse;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.stream.Collectors;

public class UserMapper {

    public static UserRepresentation toRepresentation(UserRequest request) {
        UserRepresentation u = new UserRepresentation();
        u.setUsername(request.getUsername());
        u.setEmail(request.getEmail());
        u.setFirstName(request.getFirstName());
        u.setLastName(request.getLastName());
        u.setEnabled(request.isEnabled());
        if (request.getCredentials() != null) {
            u.setCredentials(
                request.getCredentials().stream()
                    .map(UserMapper::toCredential)
                    .collect(Collectors.toList())
            );
        }
        return u;
    }

    public static CredentialRepresentation toCredential(CredentialRequest request) {
        CredentialRepresentation c = new CredentialRepresentation();
        c.setType(request.getType());
        c.setValue(request.getValue());
        c.setTemporary(request.isTemporary());
        return c;
    }

    public static UserResponse toResponse(UserRepresentation u) {
        UserResponse dto = new UserResponse();
        dto.setId(u.getId());
        dto.setUsername(u.getUsername());
        dto.setEmail(u.getEmail());
        dto.setFirstName(u.getFirstName());
        dto.setLastName(u.getLastName());
        dto.setEnabled(Boolean.TRUE.equals(u.isEnabled()));
        return dto;
    }
}
