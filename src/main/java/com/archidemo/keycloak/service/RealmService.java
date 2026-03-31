package com.archidemo.keycloak.service;

import com.archidemo.keycloak.client.RealmAdminClient;
import com.archidemo.keycloak.dto.request.RealmRequest;
import com.archidemo.keycloak.dto.response.RealmResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RealmService {

    private static final Logger log = LoggerFactory.getLogger(RealmService.class);

    private final RealmAdminClient realmClient;

    public RealmService(RealmAdminClient realmClient) {
        this.realmClient = realmClient;
    }

    public List<RealmResponse> findAll() {
        return realmClient.findAll();
    }

    public RealmResponse findByName(String realmName) {
        return realmClient.findByName(realmName);
    }

    public RealmResponse create(RealmRequest request) {
        realmClient.create(request);
        log.info("Realm créé : {}", request.getRealm());
        return realmClient.findByName(request.getRealm());
    }

    public RealmResponse update(String realmName, RealmRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("displayName", request.getDisplayName());
        body.put("enabled", request.isEnabled());
        realmClient.update(realmName, body);
        log.info("Realm mis à jour : {}", realmName);
        return realmClient.findByName(realmName);
    }

    public void delete(String realmName) {
        realmClient.delete(realmName);
        log.info("Realm supprimé : {}", realmName);
    }
}
