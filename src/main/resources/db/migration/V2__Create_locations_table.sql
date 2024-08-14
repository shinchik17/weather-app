
CREATE TABLE locations
(
    id        BIGSERIAL PRIMARY KEY,
    name      VARCHAR(128) NOT NULL,
    user_id   BIGINT       NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    latitude  DECIMAL      NOT NULL,
    longitude DECIMAL      NOT NULL,
    UNIQUE (name, latitude, longitude)
);