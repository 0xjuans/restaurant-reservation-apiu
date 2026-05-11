CREATE SCHEMA IF NOT EXISTS customers;

CREATE TABLE customers.customers (
    id         BIGSERIAL    PRIMARY KEY,
    user_id    BIGINT       NOT NULL UNIQUE,
    first_name VARCHAR(100) NOT NULL,
    last_name  VARCHAR(100) NOT NULL,
    phone      VARCHAR(20),
    created_at TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP    NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_customers_user_id ON customers.customers (user_id);
