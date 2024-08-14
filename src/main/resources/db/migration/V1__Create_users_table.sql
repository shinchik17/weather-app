
CREATE TABLE public.users
(
    id       BIGSERIAL PRIMARY KEY,
    login    VARCHAR(128) UNIQUE NOT NULL,
    password VARCHAR(64)         NOT NULL
);