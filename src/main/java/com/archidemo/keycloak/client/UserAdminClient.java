package com.archidemo.keycloak.client;

import com.archidemo.keycloak.config.KeycloakFeignConfig;
import com.archidemo.keycloak.dto.request.CredentialRequest;
import com.archidemo.keycloak.dto.request.UserRequest;
import com.archidemo.keycloak.dto.response.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@FeignClient(
        name = "user-admin-client",
        url = "${keycloak.admin.server-url}",
        configuration = KeycloakFeignConfig.class
)
public interface UserAdminClient {

    @GetMapping("/admin/realms/{realm}/users")
    List<UserResponse> findAll(@PathVariable("realm") String realm);

    @GetMapping("/admin/realms/{realm}/users/{userId}")
    UserResponse findById(@PathVariable("realm") String realm, @PathVariable("userId") String userId);

    @PostMapping("/admin/realms/{realm}/users")
    ResponseEntity<Void> create(@PathVariable("realm") String realm, @RequestBody UserRequest request);

    @PutMapping("/admin/realms/{realm}/users/{userId}")
    void update(@PathVariable("realm") String realm, @PathVariable("userId") String userId,
                @RequestBody Map<String, Object> body);

    @DeleteMapping("/admin/realms/{realm}/users/{userId}")
    void delete(@PathVariable("realm") String realm, @PathVariable("userId") String userId);

    @PutMapping("/admin/realms/{realm}/users/{userId}/reset-password")
    void resetPassword(@PathVariable("realm") String realm, @PathVariable("userId") String userId,
                       @RequestBody CredentialRequest request);
}
