--liquibase formatted sql

--changeset jgke:1

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
    sourcetype SERIAL references types(id)
);
--rollback DROP TABLE sources;

CREATE TABLE comments (
    id SERIAL UNIQUE,
    content varchar(4096),
    source SERIAL references sources(id)
);
--rollback DROP TABLE comments;

CREATE SEQUENCE hibernate_sequence;
--rollback DROP SEQUENCE hibernate_sequence;
