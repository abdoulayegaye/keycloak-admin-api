package com.archidemo.keycloak.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.List;

public class UserRequest {

    @NotBlank(message = "Le nom d'utilisateur est obligatoire")
    private String username;

    @Email(message = "L'email doit être valide")
    private String email;

    private String firstName;
    private String lastName;
    private boolean enabled = true;
    private List<CredentialRequest> credentials;

    public UserRequest() {}

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    public List<CredentialRequest> getCredentials() { return credentials; }
    public void setCredentials(List<CredentialRequest> credentials) { this.credentials = credentials; }
}
