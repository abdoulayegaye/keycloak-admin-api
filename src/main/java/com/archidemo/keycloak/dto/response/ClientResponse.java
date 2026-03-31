package com.archidemo.keycloak.dto.response;

import java.util.List;

public class ClientResponse {

    private String id;
    private String clientId;
    private String name;
    private String protocol;
    private boolean publicClient;
    private List<String> redirectUris;
    private boolean enabled;

    public ClientResponse() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
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
