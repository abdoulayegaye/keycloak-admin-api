# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Dépôt GitHub

https://github.com/abdoulayegaye/keycloak-admin-api (public)

Workflow Git :
```bash
git add <fichiers>
git commit -m "message"
git push
```

## Commands

```bash
# Démarrer PostgreSQL + Keycloak (requis avant de lancer l'application)
docker-compose up -d

# Lancer l'application
mvn spring-boot:run

# Compiler et packager
mvn clean package -DskipTests

# Lancer les tests
mvn clean test

# Lancer un test spécifique
mvn test -Dtest=NomDeLaClasse#nomDeLaMethode
```

## Architecture

Application Spring Boot 3.2 exposant une API REST qui encapsule le SDK `keycloak-admin-client` pour administrer Keycloak.

**Layers :**
- `controller/` — Points d'entrée REST (`/api/realms`, `/api/realms/{realm}/clients`, `/api/realms/{realm}/users`, `/api/realms/{realm}/roles`)
- `service/` — Logique métier, utilise le bean `Keycloak` pour interagir avec l'API Admin Keycloak
- `mapper/` — Conversion entre DTOs et représentations Keycloak (`RealmMapper`, `ClientMapper`, `UserMapper`, `RoleMapper`). Chaque mapper expose `toRepresentation(Request)` et `toResponse(Representation)` comme méthodes statiques. `UserMapper` expose aussi `toCredential(CredentialRequest)`.
- `dto/request/` et `dto/response/` — Contrats d'API (validation `@Valid` sur les requêtes, les Response DTOs sont de simples JavaBeans sans logique de mapping)
- `config/` — `KeycloakAdminConfig` crée le bean `Keycloak`, `KeycloakProperties` bind la config YAML, `SecurityConfig` configure JWT + RBAC
- `exception/` — `GlobalExceptionHandler` centralise la gestion des erreurs (4xx/5xx) avec `ErrorResponse`

**Sécurité :** Toutes les routes `/api/**` requièrent un JWT Bearer token avec le rôle `realm-admin` (extrait de `realm_access.roles` dans le token). Les routes Swagger sont publiques.

## Configuration

`src/main/resources/application.yml` :
- Serveur sur le port **8090**
- Keycloak sur **http://localhost:9090** (realm `master`, credentials `admin/admin`)
- JWT issuer : `http://localhost:9090/realms/master`

Pour obtenir un token JWT :
```bash
curl -X POST http://localhost:9090/realms/master/protocol/openid-connect/token \
  -d "client_id=admin-cli&username=admin&password=admin&grant_type=password"
```

## Documentation API

Swagger UI disponible sur `http://localhost:8090/swagger-ui.html` quand l'application tourne.

## Skill : keycloak-administration

Installé dans `.claude/skills/keycloak-administration` (symlink depuis `.agents/skills/keycloak-administration`).

Ce skill fournit une guidance complète sur l'administration Keycloak. Il est automatiquement activé quand on évoque : configuration Keycloak, SSO, OIDC, SAML, identity provider, IAM, authentication flow, user federation, realm configuration, ou access management.

Références disponibles dans `.agents/skills/keycloak-administration/references/` :
- `realm-management.md` — Configuration des realms, utilisateurs, groupes
- `client-configuration.md` — Clients OIDC/SAML, scopes, mappers
- `authentication-sso.md` — Flows d'authentification, MFA, social login, IdP
- `authorization-rbac.md` — Rôles, permissions, fine-grained authorization
- `user-federation.md` — Intégration LDAP/AD
- `security-hardening.md` — Politiques de sécurité, monitoring, audit
- `ha-scalability.md` — Clustering, performance, backup
- `troubleshooting.md` — Problèmes courants, logs, diagnostics
- `integration-examples.md` — Exemples Spring Boot, Node.js, React, Python, Docker, K8s
