--liquibase formatted sql
--changeset jgke:8

ALTER TABLE comments DROP CONSTRAINT comments_person_fk;
--rollback ALTER TABLE comments ADD CONSTRAINT comments_person_fk FOREIGN KEY(person) REFERENCES persons(id);
ALTER TABLE comments DROP CONSTRAINT comments_source_fk;
--rollback ALTER TABLE comments ADD CONSTRAINT comments_source_fk FOREIGN KEY(source) REFERENCES sources(id);
ALTER TABLE comments ADD CONSTRAINT comments_person_fk
    FOREIGN KEY(person) REFERENCES persons(id)
    ON DELETE CASCADE;
--rollback ALTER TABLE tags_sources DROP CONSTRAINT comments_person_fk;
ALTER TABLE comments ADD CONSTRAINT comments_source_fk
    FOREIGN KEY(source) REFERENCES sources(id)
    ON DELETE CASCADE;
--rollback ALTER TABLE tags_sources DROP CONSTRAINT comments_source_fk;
