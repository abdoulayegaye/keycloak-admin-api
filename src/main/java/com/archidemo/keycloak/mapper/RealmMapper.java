package com.archidemo.keycloak.mapper;

import com.archidemo.keycloak.dto.request.RealmRequest;
import com.archidemo.keycloak.dto.response.RealmResponse;
import org.keycloak.representations.idm.RealmRepresentation;

public class RealmMapper {

    public static RealmRepresentation toRepresentation(RealmRequest request) {
        RealmRepresentation r = new RealmRepresentation();
        r.setRealm(request.getRealm());
        r.setDisplayName(request.getDisplayName());
        r.setEnabled(request.isEnabled());
        return r;
    }

    public static RealmResponse toResponse(RealmRepresentation r) {
        RealmResponse dto = new RealmResponse();
        dto.setId(r.getId());
        dto.setRealm(r.getRealm());
        dto.setDisplayName(r.getDisplayName());
        dto.setEnabled(Boolean.TRUE.equals(r.isEnabled()));
        return dto;
    }
}
