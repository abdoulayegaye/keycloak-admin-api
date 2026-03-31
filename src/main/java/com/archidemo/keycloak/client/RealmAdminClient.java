package com.archidemo.keycloak.client;

import com.archidemo.keycloak.config.KeycloakFeignConfig;
import com.archidemo.keycloak.dto.request.RealmRequest;
import com.archidemo.keycloak.dto.response.RealmResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@FeignClient(
        name = "realm-admin-client",
        url = "${keycloak.admin.server-url}",
        configuration = KeycloakFeignConfig.class
)
public interface RealmAdminClient {

    @GetMapping("/admin/realms")
    List<RealmResponse> findAll();

    @GetMapping("/admin/realms/{realm}")
    RealmResponse findByName(@PathVariable("realm") String realm);

    @PostMapping("/admin/realms")
    void create(@RequestBody RealmRequest request);

    @PutMapping("/admin/realms/{realm}")
    void update(@PathVariable("realm") String realm, @RequestBody Map<String, Object> body);

    @DeleteMapping("/admin/realms/{realm}")
    void delete(@PathVariable("realm") String realm);
}
