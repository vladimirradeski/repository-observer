CREATE TABLE IF NOT EXISTS observed_repos
(
    id          UUID     DEFAULT uuid_generate_v4() PRIMARY KEY,
    created_at  TIMESTAMP WITHOUT TIME ZONE DEFAULT now(),
    license     VARCHAR(100),
    name        VARCHAR(1000),
    open_issues INTEGER,
    owner       VARCHAR(1000),
    stars       INTEGER,
    status      VARCHAR(255),
    updated_at  TIMESTAMP WITHOUT TIME ZONE,
    url         VARCHAR(255),
    version     BIGINT
);