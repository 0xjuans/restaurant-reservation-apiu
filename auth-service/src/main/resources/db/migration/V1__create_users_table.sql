CREATE SCHEMA IF NOT EXISTS auth;

CREATE TABLE auth.users (
    id         BIGSERIAL PRIMARY KEY,
    email      VARCHAR(150) NOT NULL UNIQUE,
    password   VARCHAR(255) NOT NULL,
    enabled    BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP    NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_users_email ON auth.users (email);
