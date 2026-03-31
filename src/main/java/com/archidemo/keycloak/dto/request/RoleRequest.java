package com.archidemo.keycloak.dto.request;

import jakarta.validation.constraints.NotBlank;

public class RoleRequest {

    @NotBlank(message = "Le nom du rôle est obligatoire")
    private String name;

    private String description;

    public RoleRequest() {}

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
