-- liquibase formatted sql

-- changeset agorohov:1
CREATE TABLE wallets (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    balance NUMERIC(19, 2) NOT NULL
);

-- changeset agorohov:2
INSERT INTO wallets (id, balance)
    VALUES
    (DEFAULT, 3500);