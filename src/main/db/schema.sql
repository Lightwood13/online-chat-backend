CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE users
(
    id                     uuid PRIMARY KEY DEFAULT uuid_generate_v4(),
    username               varchar(32) NOT NUll UNIQUE,
    name                   varchar(64) NOT NULL,
    encrypted_password     text        NOT NULL,
    profile_photo_location text
);

CREATE TABLE friend
(
    user_id   uuid NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    friend_id uuid NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT friends_pkey PRIMARY KEY (user_id, friend_id)
);

CREATE TABLE group_chat
(
    id                     uuid PRIMARY KEY DEFAULT uuid_generate_v4(),
    name                   varchar(64) NOT NULL,
    profile_photo_location text
);

CREATE TABLE user_group_chat
(
    user_id       uuid NOT NULL REFERENCES users (id)      ON DELETE CASCADE,
    group_chat_id uuid NOT NULL REFERENCES group_chat (id) ON DELETE CASCADE,
    CONSTRAINT user_group_chat_pkey PRIMARY KEY (user_id, group_chat_id)
);

CREATE TABLE message
(
    id            uuid PRIMARY KEY DEFAULT uuid_generate_v4(),
    group_chat_id uuid         NOT NULL REFERENCES group_chat (id),
    -- TODO: add ability to delete users
    author_id     uuid         NOT NULL REFERENCES users (id),
    text          varchar(256) NOT NULL,
    sent_on       timestamp    NOT NULL
);