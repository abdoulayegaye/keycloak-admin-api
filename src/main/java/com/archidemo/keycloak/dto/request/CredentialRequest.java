package com.archidemo.keycloak.dto.request;

import jakarta.validation.constraints.NotBlank;

public class CredentialRequest {

    private String type = "password";

    @NotBlank(message = "La valeur du mot de passe est obligatoire")
    private String value;

    private boolean temporary = false;

    public CredentialRequest() {}

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }
    public boolean isTemporary() { return temporary; }
    public void setTemporary(boolean temporary) { this.temporary = temporary; }
}
