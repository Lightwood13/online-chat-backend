CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS users
(
    id                 uuid PRIMARY KEY DEFAULT uuid_generate_v4(),
    username           varchar(32) NOT NUll UNIQUE,
    name               varchar(64) NOT NULL,
    encrypted_password TEXT        NOT NULL
);

CREATE TABLE IF NOT EXISTS messages
(
    id        uuid PRIMARY KEY DEFAULT uuid_generate_v4(),
    author_id uuid         NOT NULL, -- TODO: add ability to delete users
    text      varchar(256) NOT NULL,
    sent_on   timestamp    NOT NULL,

    CONSTRAINT fk_author FOREIGN KEY (author_id) REFERENCES users (id)
);