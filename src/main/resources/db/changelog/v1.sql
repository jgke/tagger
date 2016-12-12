--liquibase formatted sql

--changeset jgke:1

create table users (
    id SERIAL UNIQUE,
    username varchar(255),
    email varchar(255),
    password varchar(255)
);
--rollback DROP TABLE users;

CREATE TABLE types (
    id SERIAL UNIQUE,
    value varchar(255)
);
--rollback DROP TABLE types;
INSERT INTO types (value) VALUES ('image'), ('html'), ('video'), ('sound');

CREATE TABLE sources (
    id SERIAL UNIQUE,
    title varchar(255),
    url varchar(4096),
    sourcetype SERIAL references types(id),
    submitter SERIAL references users(id)
);
--rollback DROP TABLE sources;

CREATE TABLE comments (
    id SERIAL UNIQUE,
    content varchar(4096),
    source SERIAL references sources(id),
    submitter SERIAL references users(id)
);
--rollback DROP TABLE comments;

CREATE SEQUENCE hibernate_sequence;
--rollback DROP SEQUENCE hibernate_sequence;
