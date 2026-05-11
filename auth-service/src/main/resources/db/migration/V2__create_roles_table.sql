CREATE TABLE auth.roles (
    id   BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE auth.user_roles (
    user_id BIGINT NOT NULL REFERENCES auth.users (id) ON DELETE CASCADE,
    role_id BIGINT NOT NULL REFERENCES auth.roles (id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, role_id)
);

INSERT INTO auth.roles (name) VALUES ('ROLE_ADMIN'), ('ROLE_STAFF'), ('ROLE_CUSTOMER');
