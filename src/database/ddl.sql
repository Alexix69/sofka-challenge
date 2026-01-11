CREATE DATABASE "backend-challenge";

CREATE TYPE gender_type AS ENUM ('MALE', 'FEMALE');
CREATE TYPE account_type_enum AS ENUM ('SAVINGS', 'CHECKING');
CREATE TYPE transaction_type_enum AS ENUM ('DEPOSIT', 'WITHDRAWAL', 'TRANSFER');

CREATE TABLE person (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    gender gender_type,
    age INT,
    identification VARCHAR(50) UNIQUE NOT NULL,
    address VARCHAR(255),
    phone VARCHAR(20)
);

CREATE TABLE client (
    id SERIAL PRIMARY KEY,
    person_id INT UNIQUE NOT NULL REFERENCES person(id) ON DELETE CASCADE,
    password_hash VARCHAR(255) NOT NULL,
    status BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE account (
    id SERIAL PRIMARY KEY,
    account_number VARCHAR(20) NOT NULL,
    account_type account_type_enum NOT NULL,
    initial_balance DECIMAL(10,2) NOT NULL,
    status BOOLEAN NOT NULL DEFAULT TRUE,
    client_id INT NOT NULL REFERENCES client(id) ON DELETE CASCADE,
    CONSTRAINT unique_account_number UNIQUE (account_number)
);

CREATE INDEX idx_account_client_id ON account(client_id);

CREATE TABLE account_transaction (
    id SERIAL PRIMARY KEY,
    transaction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    transaction_type transaction_type_enum NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    balance DECIMAL(10,2) NOT NULL,
    account_id INT NOT NULL REFERENCES account(id) ON DELETE RESTRICT
);

CREATE INDEX idx_account_transaction_account_id ON account_transaction(account_id);
