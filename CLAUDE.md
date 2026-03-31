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

Application Spring Boot 3.2 exposant une API REST qui appelle l'Admin REST API de Keycloak via **OpenFeign** (branche `feature/openfeign`).

**Layers :**
- `controller/` — Points d'entrée REST (`/api/realms`, `/api/realms/{realm}/clients`, `/api/realms/{realm}/users`, `/api/realms/{realm}/roles`)
- `service/` — Logique métier, délègue aux clients Feign
- `client/` — Interfaces Feign (`RealmAdminClient`, `ClientAdminClient`, `UserAdminClient`, `RoleAdminClient`) appelant directement `GET/POST/PUT/DELETE /admin/realms/...`. Les POST create retournent `ResponseEntity<Void>` pour extraire l'ID depuis le header `Location`.
- `dto/request/` et `dto/response/` — Contrats d'API, utilisés directement comme corps de requête/réponse Feign
- `config/` — `KeycloakTokenManager` (token admin en cache via RestTemplate), `KeycloakFeignConfig` (intercepteur Bearer, sans `@Configuration`), `KeycloakProperties`, `SecurityConfig`
- `exception/` — `GlobalExceptionHandler` gère `FeignException` (et ses sous-types `NotFound`, `Forbidden`) + validation

**Branches :**
- `main` — version avec `keycloak-admin-client` SDK + package `mapper/`
- `feature/openfeign` — version OpenFeign sans SDK Keycloak, sans `mapper/`

**Sécurité :** Toutes les routes `/api/**` requièrent un JWT Bearer token avec le rôle `admin` **ou** `realm-admin` (extraits de `realm_access.roles` dans le token). L'utilisateur `admin` de Keycloak a le rôle `admin` (pas `realm-admin` qui est un rôle client de `realm-management`). Les routes Swagger sont publiques.

## Configuration

`src/main/resources/application.yml` :
- Serveur sur le port **8090**
- Keycloak sur **http://localhost:9090** (realm `master`, credentials `admin/admin`)
- JWT issuer : `http://localhost:9090/realms/master`
- Durée du token d'accès : **3600 secondes** (1 heure), configurée automatiquement par le service `keycloak-init` au démarrage via `kcadm.sh`

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
