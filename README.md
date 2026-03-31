# Keycloak Admin API

Application Spring Boot 3 exposant une REST API sécurisée pour administrer Keycloak
via le SDK `keycloak-admin-client`.

## Stack

| Couche | Technologie |
|---|---|
| Framework | Spring Boot 3.2.5 |
| Sécurité | Spring Security + OAuth2 Resource Server (JWT) |
| Client Admin | keycloak-admin-client 24.0.3 |
| Build | Maven |
| Doc API | Springdoc OpenAPI (Swagger UI) |

## Prérequis

- Java 17+
- Maven 3.8+
- Docker + Docker Compose

## Lancement

### 1. Démarrer Keycloak

```bash
docker-compose up -d
```

Keycloak démarre sur **http://localhost:9090**
Console d'administration : http://localhost:9090/admin (admin / admin)

### 2. Compiler et lancer l'application

```bash
mvn clean package -DskipTests
mvn spring-boot:run
```

L'API démarre sur **http://localhost:8090**

### 3. Obtenir un token JWT

```bash
curl -s -X POST http://localhost:9090/realms/master/protocol/openid-connect/token \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "grant_type=password&client_id=admin-cli&username=admin&password=admin" \
  | jq -r '.access_token'
```

### 4. Appeler l'API

```bash
TOKEN=$(curl -s -X POST http://localhost:9090/realms/master/protocol/openid-connect/token \
  -d "grant_type=password&client_id=admin-cli&username=admin&password=admin" \
  | jq -r '.access_token')

# Lister tous les realms
curl -H "Authorization: Bearer $TOKEN" http://localhost:8090/api/realms
```

## Documentation Swagger

Accessible sur : **http://localhost:8090/swagger-ui.html**

Dans Swagger UI :
1. Cliquer sur **Authorize**
2. Saisir `Bearer <votre_token>`

## Endpoints disponibles

### Module 1 — Realms
| Méthode | Endpoint | Description |
|---|---|---|
| GET | `/api/realms` | Lister tous les realms |
| GET | `/api/realms/{realmName}` | Récupérer un realm |
| POST | `/api/realms` | Créer un realm |
| PUT | `/api/realms/{realmName}` | Mettre à jour un realm |
| DELETE | `/api/realms/{realmName}` | Supprimer un realm |

### Module 2 — Clients OAuth2
| Méthode | Endpoint | Description |
|---|---|---|
| GET | `/api/realms/{realmName}/clients` | Lister les clients |
| GET | `/api/realms/{realmName}/clients/{id}` | Récupérer un client |
| POST | `/api/realms/{realmName}/clients` | Créer un client |
| PUT | `/api/realms/{realmName}/clients/{id}` | Mettre à jour un client |
| DELETE | `/api/realms/{realmName}/clients/{id}` | Supprimer un client |

### Module 3 — Utilisateurs
| Méthode | Endpoint | Description |
|---|---|---|
| GET | `/api/realms/{realmName}/users` | Lister les utilisateurs |
| GET | `/api/realms/{realmName}/users/{userId}` | Récupérer un utilisateur |
| POST | `/api/realms/{realmName}/users` | Créer un utilisateur |
| PUT | `/api/realms/{realmName}/users/{userId}` | Mettre à jour un utilisateur |
| DELETE | `/api/realms/{realmName}/users/{userId}` | Supprimer un utilisateur |
| PUT | `/api/realms/{realmName}/users/{userId}/resetpassword` | Réinitialiser le mot de passe |

### Module 4 — Rôles
| Méthode | Endpoint | Description |
|---|---|---|
| GET | `/api/realms/{realmName}/roles` | Lister les rôles |
| POST | `/api/realms/{realmName}/roles` | Créer un rôle |
| DELETE | `/api/realms/{realmName}/roles/{roleName}` | Supprimer un rôle |
| POST | `/api/realms/{realmName}/users/{userId}/roles` | Assigner des rôles |
| GET | `/api/realms/{realmName}/users/{userId}/roles` | Rôles d'un utilisateur |
| DELETE | `/api/realms/{realmName}/users/{userId}/roles` | Retirer des rôles |

## Sécurité

- Tous les endpoints `/api/**` requièrent un Bearer JWT valide
- Le token est validé contre l'issuer `http://localhost:9090/realms/master`
- Seul un utilisateur ayant le rôle **`realm-admin`** peut accéder aux endpoints
- Les routes Swagger (`/swagger-ui/**`, `/v3/api-docs/**`) sont publiques
