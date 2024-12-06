CREATE TABLE IF NOT EXISTS users_clients (
    id BIGSERIAL PRIMARY KEY,
    user_name VARCHAR(255) UNIQUE,
    full_name VARCHAR(255),
    password VARCHAR(255),
    account_non_expired BOOLEAN DEFAULT TRUE,
    account_non_locked BOOLEAN DEFAULT TRUE,
    credentials_non_expired BOOLEAN DEFAULT TRUE,
    enabled BOOLEAN DEFAULT TRUE
);