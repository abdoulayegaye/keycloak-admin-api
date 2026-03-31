package com.archidemo.keycloak.mapper;

import com.archidemo.keycloak.dto.request.RoleRequest;
import com.archidemo.keycloak.dto.response.RoleResponse;
import org.keycloak.representations.idm.RoleRepresentation;

public class RoleMapper {

    public static RoleRepresentation toRepresentation(RoleRequest request) {
        RoleRepresentation r = new RoleRepresentation();
        r.setName(request.getName());
        r.setDescription(request.getDescription());
        return r;
    }

    public static RoleResponse toResponse(RoleRepresentation r) {
        RoleResponse dto = new RoleResponse();
        dto.setId(r.getId());
        dto.setName(r.getName());
        dto.setDescription(r.getDescription());
        return dto;
    }
}
