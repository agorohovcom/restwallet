-- liquibase formatted sql

-- changeset agorohov:1
CREATE TABLE wallet (
    id UUID PRIMARY KEY,
    balance NUMERIC(19, 2) NOT NULL
);