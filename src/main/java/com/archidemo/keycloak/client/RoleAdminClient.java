package com.archidemo.keycloak.client;

import com.archidemo.keycloak.config.KeycloakFeignConfig;
import com.archidemo.keycloak.dto.request.RoleRequest;
import com.archidemo.keycloak.dto.response.RoleResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(
        name = "role-admin-client",
        url = "${keycloak.admin.server-url}",
        configuration = KeycloakFeignConfig.class
)
public interface RoleAdminClient {

    @GetMapping("/admin/realms/{realm}/roles")
    List<RoleResponse> findAllRealmRoles(@PathVariable("realm") String realm);

    @GetMapping("/admin/realms/{realm}/roles/{roleName}")
    RoleResponse findByName(@PathVariable("realm") String realm, @PathVariable("roleName") String roleName);

    @PostMapping("/admin/realms/{realm}/roles")
    void createRealmRole(@PathVariable("realm") String realm, @RequestBody RoleRequest request);

    @DeleteMapping("/admin/realms/{realm}/roles/{roleName}")
    void deleteRealmRole(@PathVariable("realm") String realm, @PathVariable("roleName") String roleName);

    @PostMapping("/admin/realms/{realm}/users/{userId}/role-mappings/realm")
    void assignRolesToUser(@PathVariable("realm") String realm, @PathVariable("userId") String userId,
                           @RequestBody List<RoleResponse> roles);

    @GetMapping("/admin/realms/{realm}/users/{userId}/role-mappings/realm")
    List<RoleResponse> getUserRoles(@PathVariable("realm") String realm, @PathVariable("userId") String userId);

    @DeleteMapping("/admin/realms/{realm}/users/{userId}/role-mappings/realm")
    void removeRolesFromUser(@PathVariable("realm") String realm, @PathVariable("userId") String userId,
                             @RequestBody List<RoleResponse> roles);
}
