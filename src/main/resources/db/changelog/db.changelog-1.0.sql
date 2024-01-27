--liquibase formatted sql

--changeset danillkucheruk:1
CREATE TABLE IF NOT EXISTS users (
    id bigserial primary key,
    username varchar(255) unique not null,
    password varchar(255) not null
);

--changeset danillkucheruk:2
CREATE TABLE IF NOT EXISTS lists (
    id bigserial primary key,
    user_id bigint not null references users(id) on delete cascade,
    title varchar(255) not null,
    description varchar(255) not null
);

--changeset danillkucheruk:3
CREATE TABLE IF NOT EXISTS notes (
   id bigserial primary key,
   list_id bigint not null references lists(id) on delete cascade,
   title varchar(255) not null,
   content varchar(8000) not null
);
