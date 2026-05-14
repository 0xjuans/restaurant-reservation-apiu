CREATE SCHEMA IF NOT EXISTS tables;

-- Catálogo de zonas del restaurante (interior, terraza, etc.)
CREATE TABLE tables.zones (
    id          BIGSERIAL    PRIMARY KEY,
    name        VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(255),
    created_at  TIMESTAMP    NOT NULL DEFAULT NOW()
);

INSERT INTO tables.zones (name, description) VALUES
    ('Interior', 'Mesas dentro del restaurante'),
    ('Terraza',  'Mesas al aire libre'),
    ('Bar',      'Zona de barra y cócteles'),
    ('Privado',  'Salón privado para eventos');

-- Mesas físicas, cada una pertenece a una zona
CREATE TABLE tables.restaurant_tables (
    id         BIGSERIAL    PRIMARY KEY,
    zone_id    BIGINT       NOT NULL REFERENCES tables.zones(id),
    number     INTEGER      NOT NULL,
    capacity   INTEGER      NOT NULL CHECK (capacity > 0),
    status     VARCHAR(20)  NOT NULL DEFAULT 'AVAILABLE',
    created_at TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP    NOT NULL DEFAULT NOW(),
    UNIQUE (zone_id, number)
);

CREATE INDEX idx_tables_zone_id ON tables.restaurant_tables(zone_id);
CREATE INDEX idx_tables_status  ON tables.restaurant_tables(status);

INSERT INTO tables.restaurant_tables (zone_id, number, capacity) VALUES
    (1, 1, 2), (1, 2, 2), (1, 3, 4), (1, 4, 4), (1, 5, 6),
    (2, 1, 4), (2, 2, 4), (2, 3, 6),
    (3, 1, 2), (3, 2, 2),
    (4, 1, 10),(4, 2, 20);
