CREATE SCHEMA IF NOT EXISTS tables;

CREATE TABLE tables.zones (
    id          BIGSERIAL    PRIMARY KEY,
    name        VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(255),
    created_at  TIMESTAMP    NOT NULL DEFAULT NOW()
);

INSERT INTO tables.zones (name, description) VALUES
    ('Interior',  'Mesas dentro del restaurante'),
    ('Terraza',   'Mesas al aire libre'),
    ('Bar',       'Zona de barra y cócteles'),
    ('Privado',   'Salón privado para eventos');
