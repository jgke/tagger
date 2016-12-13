--liquibase formatted sql

--changeset jgke:2

CREATE TABLE tags (
    id SERIAL UNIQUE,
    value varchar(64)
);
--rollback DROP TABLE tags;

CREATE TABLE tags_sources (
    source SERIAL references sources(id),
    tag SERIAL references tags(id)
);
--rollback DROP TABLE tags_sources;
