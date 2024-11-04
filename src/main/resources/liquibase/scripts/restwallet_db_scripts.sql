-- liquibase formatted sql

-- changeset agorohov:1
CREATE TABLE wallet (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    balance NUMERIC(19, 2) NOT NULL
);

-- changeset agorohov:2
INSERT INTO wallet (id, balance)
    VALUES
    (DEFAULT, 3500),
    (DEFAULT, 0),
    (DEFAULT, 100.500),
    (DEFAULT, 1.123456789);