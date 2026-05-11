# 🍽️ Restaurant Reservation APIU

Professional restaurant reservation system built with Spring Boot 3, PostgreSQL, Redis, Docker, JWT security, and Flyway migrations.

---

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Backend | Spring Boot 3.x (Java 21) |
| Database | PostgreSQL 16 |
| Cache / Sessions | Redis 7 |
| Containers | Docker + Docker Compose |
| Security | Spring Security + JWT |
| ORM | Spring Data JPA + Hibernate |
| DB Migrations | Flyway |
| API Docs | Swagger / OpenAPI 3 |
| Testing | JUnit 5 + Mockito + Testcontainers |
| Build | Maven |

---

## Modules

- **Auth** — Register, login, logout, roles (ADMIN, STAFF, CUSTOMER)
- **Tables** — Manage tables by capacity and zone
- **Reservations** — Create, update, cancel, confirm
- **Customers** — Profile and reservation history

---

## Getting Started

### Prerequisites
- Docker Desktop
- Java 21
- Maven 3.9+

### Run with Docker
```bash
docker-compose up -d
```

### Run the app
```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=local
```

---

## Branch Strategy

| Branch | Purpose |
|--------|---------|
| `main` | Production-ready code |
| `develop` | Integration branch |
| `feature/*` | New features |
| `fix/*` | Bug fixes |
| `release/*` | Release preparation |

---

## Environment Variables

Copy `.env.example` to `.env` and fill in the values (never commit `.env`).

---

## API Documentation

Once running, visit: `http://localhost:8080/swagger-ui.html`
