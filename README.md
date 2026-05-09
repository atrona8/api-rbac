# API-RBAC - Role-Based Access Control API

Ce projet est un API REST sécurisée de gestion des autorisations (RBAC) construite avec Spring Boot 4,
Spring Security 7 et JWT. Projet portfolio démontrant une architecture modulaire,
un système d'audit asynchrone et des bonnes pratiques de sécurité.

## Stack technique

- **Java 17** + **Spring Boot 4.0.1**
- **Spring Security 7** : JWT stateless, BCrypt
- **PostgreSQL 16** : base de données
- **Redis 7** : blacklist de tokens et cache de sessions
- **Flyway** : migrations versionnées
- **Spring Events + @Async** : audit
- **OpenAPI 3 / Swagger UI** : documentation automatique
- **Docker Compose** : déploiement 
- **TestContainers** : tests d'intégration 

## Architecture

```
api_rbac/
├── auth/          # JWT, login, register
├── rbac/          # Users, Roles, Permissions (cœur métier)
├── audit/         # Logs d'accès asynchrones
├── config/        # Spring Security, Redis, OpenAPI
└── common/        # Exceptions, ApiResponse<T>, Métriques
```

## Démarrage rapide

### Prérequis
- Docker + Docker Compose

### Lancer le projet

```bash
# 1. Cloner le repo
git clone https://github.com/omar/api-rbac.git
cd api-rbac

# 2. Builder l'application
./mvnw clean package -DskipTests

# 3. Lancer tous les services
docker compose up -d

# 4. Vérifier que tout tourne
docker compose ps
```

L'API est disponible sur `http://localhost:8080`
Swagger UI : `http://localhost:8080/swagger-ui/index.html`

## Endpoints principaux

### Authentification
| Méthode | Endpoint | Description |
|---------|----------|-------------|
| POST | `/api/v1/auth/register` | Créer un compte |
| POST | `/api/v1/auth/login` | Obtenir un JWT |

### Gestion RBAC (ADMIN uniquement)
| Méthode | Endpoint | Description |
|---------|----------|-------------|
| GET | `/api/v1/users` | Lister les utilisateurs |
| POST | `/api/v1/users/{id}/roles/{roleId}` | Assigner un rôle |
| GET | `/api/v1/roles` | Lister les rôles |
| POST | `/api/v1/roles` | Créer un rôle |
| POST | `/api/v1/roles/{id}/permissions/{permId}` | Assigner une permission |
| GET | `/api/v1/permissions` | Lister les permissions |
| GET | `/api/v1/audit` | Consulter les logs d'accès |

## Exemple d'utilisation

```bash
# Register
curl -X POST http://localhost:8080/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"omar","email":"omar@example.com","password":"password123"}'

# Login → récupérer le token
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"omar","password":"password123"}'

# Utiliser le token
curl http://localhost:8080/api/v1/users \
  -H "Authorization: Bearer <token>"
```

## Décisions techniques

**Pourquoi Spring Events pour l'audit ?**
L'audit ne doit jamais bloquer une requête. Avec `@EventListener` + `@Async`,
chaque accès est loggué dans un thread séparé pour ne pas impacter la latence.

**Pourquoi JWT stateless et Redis ?**
JWT seul ne permet pas la révocation. Et Redis stocke le blacklist des tokens
invalidés.

**Pourquoi Flyway ?**
Les migrations versionnées permettent que la structure de la base est
toujours synchronisée avec le code, en dev comme en prod. 
