--liquibase formatted sql
--changeset jgke:13

ALTER TABLE sources DROP CONSTRAINT sources_person_fkey;
--rollback ALTER TABLE sources ADD CONSTRAINT sources_person_fkey FOREIGN KEY (person) REFERENCES persons(id);

ALTER TABLE sources
    ADD CONSTRAINT sources_person_fkey
    FOREIGN KEY (person) REFERENCES persons(id) ON DELETE SET NULL;
--rollback ALTER TABLE sources DROP CONSTRAINT sources_person_fkey;

DELETE FROM persons WHERE id=1;
--rollback INSERT INTO persons VALUES (1, '[deleted]', '', '');