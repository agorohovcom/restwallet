-- liquibase formatted sql

-- changeset agorohov:1
CREATE TABLE wallet (
    id UUID PRIMARY KEY,
    operation_type VARCHAR(255),
    balance NUMERIC(19, 2) NOT NULL
);