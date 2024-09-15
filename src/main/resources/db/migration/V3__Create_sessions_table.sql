CREATE TABLE sessions
(
    id         VARCHAR(128) PRIMARY KEY,
    user_id    BIGINT UNIQUE NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    expires_at TIMESTAMP     NOT NULL
);