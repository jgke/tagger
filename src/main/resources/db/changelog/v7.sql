--liquibase formatted sql
--changeset jgke:7

CREATE TABLE comments (
    id SERIAL UNIQUE,
    comment varchar(4000),
    person integer,
    source integer,
    CONSTRAINT comments_person_fk
    FOREIGN KEY (person) REFERENCES persons(id),
    CONSTRAINT comments_source_fk
    FOREIGN KEY (source) REFERENCES sources(id)
);
--rollback DROP TABLE comments;
