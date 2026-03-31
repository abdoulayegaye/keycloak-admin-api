package com.archidemo.keycloak.dto.request;

import jakarta.validation.constraints.NotBlank;
import java.util.List;

public class ClientRequest {

    @NotBlank(message = "Le clientId est obligatoire")
    private String clientId;

    private String name;
    private String protocol = "openid-connect";
    private boolean publicClient = false;
    private List<String> redirectUris;
    private boolean enabled = true;

    public ClientRequest() {}

    public String getClientId() { return clientId; }
    public void setClientId(String clientId) { this.clientId = clientId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getProtocol() { return protocol; }
    public void setProtocol(String protocol) { this.protocol = protocol; }
    public boolean isPublicClient() { return publicClient; }
    public void setPublicClient(boolean publicClient) { this.publicClient = publicClient; }
    public List<String> getRedirectUris() { return redirectUris; }
    public void setRedirectUris(List<String> redirectUris) { this.redirectUris = redirectUris; }
    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
}
