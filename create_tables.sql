CREATE TABLE users
(
    id       BIGSERIAL PRIMARY KEY,
    login    VARCHAR(128) UNIQUE NOT NULL,
    password VARCHAR(64)         NOT NULL
);

CREATE TABLE locations
(
    id        BIGSERIAL PRIMARY KEY,
    name      VARCHAR(128) NOT NULL,
    user_id   BIGINT       NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    latitude  DECIMAL      NOT NULL,
    longitude DECIMAL      NOT NULL,
    UNIQUE (name, latitude, longitude)
);


CREATE TABLE sessions
(
    id         VARCHAR(128) PRIMARY KEY,
    user_id    BIGINT    NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    expires_at TIMESTAMP NOT NULL,
    UNIQUE (id, user_id)
);

DROP TABLE users;