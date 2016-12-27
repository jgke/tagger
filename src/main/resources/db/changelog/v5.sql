--liquibase formatted sql
--changeset jgke:5

CREATE TABLE persons (
    id SERIAL UNIQUE,
    username varchar(100) UNIQUE,
    password varchar(100),
    salt varchar(100)
);
--rollback DROP TABLE persons;
INSERT INTO persons VALUES (1, '[deleted]', '', '');

ALTER TABLE sources
    ADD person integer DEFAULT 1;
ALTER TABLE sources
    ADD CONSTRAINT sources_person_fkey
    FOREIGN KEY (person) REFERENCES persons(id);
--rollback ALTER TABLE tags_sources DROP COLUMN person;
