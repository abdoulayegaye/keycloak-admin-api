package com.archidemo.keycloak.dto.request;

import jakarta.validation.constraints.NotBlank;

public class RealmRequest {

    @NotBlank(message = "Le nom du realm est obligatoire")
    private String realm;

    private String displayName;
    private boolean enabled = true;

    public RealmRequest() {}

    public String getRealm() { return realm; }
    public void setRealm(String realm) { this.realm = realm; }
    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }
    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
}
