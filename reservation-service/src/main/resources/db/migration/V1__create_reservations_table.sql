CREATE SCHEMA IF NOT EXISTS reservations;

CREATE TABLE reservations.reservations (
    id           BIGSERIAL    PRIMARY KEY,
    customer_id  BIGINT       NOT NULL,
    table_id     BIGINT       NOT NULL,
    date         DATE         NOT NULL,
    start_time   TIME         NOT NULL,
    end_time     TIME         NOT NULL,
    guests_count INTEGER      NOT NULL CHECK (guests_count > 0),
    -- VARCHAR en lugar de ENUM para compatibilidad con Hibernate @Enumerated(STRING)
    status       VARCHAR(20)  NOT NULL DEFAULT 'PENDING',
    notes        VARCHAR(500),
    created_at   TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at   TIMESTAMP    NOT NULL DEFAULT NOW(),
    CONSTRAINT chk_time_range CHECK (end_time > start_time)
);

CREATE INDEX idx_reservations_customer_id ON reservations.reservations (customer_id);
CREATE INDEX idx_reservations_table_id    ON reservations.reservations (table_id);
CREATE INDEX idx_reservations_date        ON reservations.reservations (date);
CREATE INDEX idx_reservations_status      ON reservations.reservations (status);
