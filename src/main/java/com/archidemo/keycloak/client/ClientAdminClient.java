package com.archidemo.keycloak.client;

import com.archidemo.keycloak.config.KeycloakFeignConfig;
import com.archidemo.keycloak.dto.request.ClientRequest;
import com.archidemo.keycloak.dto.response.ClientResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(
        name = "client-admin-client",
        url = "${keycloak.admin.server-url}",
        configuration = KeycloakFeignConfig.class
)
public interface ClientAdminClient {

    @GetMapping("/admin/realms/{realm}/clients")
    List<ClientResponse> findAll(@PathVariable("realm") String realm);

    @GetMapping("/admin/realms/{realm}/clients/{id}")
    ClientResponse findById(@PathVariable("realm") String realm, @PathVariable("id") String id);

    @PostMapping("/admin/realms/{realm}/clients")
    ResponseEntity<Void> create(@PathVariable("realm") String realm, @RequestBody ClientRequest request);

    @PutMapping("/admin/realms/{realm}/clients/{id}")
    void update(@PathVariable("realm") String realm, @PathVariable("id") String id,
                @RequestBody ClientRequest request);

    @DeleteMapping("/admin/realms/{realm}/clients/{id}")
    void delete(@PathVariable("realm") String realm, @PathVariable("id") String id);
}
