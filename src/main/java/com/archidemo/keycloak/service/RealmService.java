package com.archidemo.keycloak.service;

import com.archidemo.keycloak.dto.request.RealmRequest;
import com.archidemo.keycloak.dto.response.RealmResponse;
import com.archidemo.keycloak.mapper.RealmMapper;
import org.keycloak.admin.client.Keycloak;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RealmService {

    private static final Logger log = LoggerFactory.getLogger(RealmService.class);

    @Autowired
    private Keycloak keycloak;

    public List<RealmResponse> findAll() {
        return keycloak.realms().findAll()
                .stream()
                .map(RealmMapper::toResponse)
                .collect(Collectors.toList());
    }

    public RealmResponse findByName(String realmName) {
        return RealmMapper.toResponse(keycloak.realm(realmName).toRepresentation());
    }

    public RealmResponse create(RealmRequest request) {
        keycloak.realms().create(RealmMapper.toRepresentation(request));
        log.info("Realm créé : {}", request.getRealm());
        return RealmMapper.toResponse(keycloak.realm(request.getRealm()).toRepresentation());
    }

    public RealmResponse update(String realmName, RealmRequest request) {
        var existing = keycloak.realm(realmName).toRepresentation();
        existing.setDisplayName(request.getDisplayName());
        existing.setEnabled(request.isEnabled());
        keycloak.realm(realmName).update(existing);
        log.info("Realm mis à jour : {}", realmName);
        return RealmMapper.toResponse(keycloak.realm(realmName).toRepresentation());
    }

    public void delete(String realmName) {
        keycloak.realm(realmName).remove();
        log.info("Realm supprimé : {}", realmName);
    }
}
