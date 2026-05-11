# 🍽️ Restaurant Reservation APIU

Sistema profesional de reservas para restaurante construido con Spring Boot 3, PostgreSQL, Redis, Docker, seguridad JWT y migraciones con Flyway.

---

## Stack Tecnológico

| Capa | Tecnología |
|------|-----------|
| Backend | Spring Boot 3.x (Java 21) |
| Base de datos | PostgreSQL 16 |
| Caché / Sesiones | Redis 7 |
| Contenedores | Docker + Docker Compose |
| Seguridad | Spring Security + JWT |
| ORM | Spring Data JPA + Hibernate |
| Migraciones BD | Flyway |
| Documentación API | Swagger / OpenAPI 3 |
| Pruebas | JUnit 5 + Mockito + Testcontainers |
| Build | Maven |

---

## Módulos

- **Autenticación** — Registro, login, logout, roles (ADMIN, STAFF, CUSTOMER)
- **Mesas** — Gestión de mesas por capacidad y zona
- **Reservas** — Crear, modificar, cancelar, confirmar
- **Clientes** — Perfil e historial de reservas

---

## Inicio Rápido

### Requisitos previos
- Docker Desktop
- Java 21
- Maven 3.9+

### Levantar con Docker
```bash
docker-compose up -d
```

### Ejecutar la aplicación
```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=local
```

---

## Estrategia de Ramas

| Rama | Propósito |
|------|----------|
| `main` | Código listo para producción |
| `develop` | Rama de integración |
| `feature/*` | Nuevas funcionalidades |
| `fix/*` | Corrección de errores |
| `release/*` | Preparación de versiones |

---

## Variables de Entorno

Copia `.env.example` a `.env` y completa los valores (nunca commitees `.env`).

---

## Documentación de la API

Una vez que la aplicación esté corriendo, visita: `http://localhost:8080/swagger-ui.html`
