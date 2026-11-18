CREATE TABLE accounts (
    id  BIGSERIAL PRIMARY KEY,
    account_number VARCHAR(20) NOT NULL UNIQUE,
    owner_username VARCHAR(100) NOT NULL,
    currency VARCHAR(3) NOT NULL,
    balance NUMERIC(19,3) NOT NULL DEFAULT 0 CHECK (balance >=0),
    status VARCHAR(29) NOT NULL,
    version BIGINT NOT NULL DEFAULT 0,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE INDEX idx_accounts_owner ON accounts (owner_username);