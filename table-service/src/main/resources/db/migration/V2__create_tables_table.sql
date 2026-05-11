CREATE TYPE tables.table_status AS ENUM ('AVAILABLE', 'OCCUPIED', 'MAINTENANCE');

CREATE TABLE tables.tables (
    id         BIGSERIAL          PRIMARY KEY,
    zone_id    BIGINT             NOT NULL REFERENCES tables.zones (id),
    number     INTEGER            NOT NULL,
    capacity   INTEGER            NOT NULL CHECK (capacity > 0),
    status     tables.table_status NOT NULL DEFAULT 'AVAILABLE',
    created_at TIMESTAMP          NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP          NOT NULL DEFAULT NOW(),
    UNIQUE (zone_id, number)
);

CREATE INDEX idx_tables_zone_id ON tables.tables (zone_id);
CREATE INDEX idx_tables_status  ON tables.tables (status);

INSERT INTO tables.tables (zone_id, number, capacity) VALUES
    (1, 1, 2), (1, 2, 2), (1, 3, 4), (1, 4, 4), (1, 5, 6),
    (2, 1, 4), (2, 2, 4), (2, 3, 6),
    (3, 1, 2), (3, 2, 2),
    (4, 1, 10),(4, 2, 20);
