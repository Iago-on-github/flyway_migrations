ALTER TABLE users_data

ALTER COLUMN key SET DEFAULT gen_random_uuid();

CREATE EXTENSION IF NOT EXISTS pgcrypto;
