--liquibase formatted sql
--changeset jgke:10

DELETE FROM types WHERE value='sound';
--rollback INSERT INTO types (value) VALUES ('sound');

UPDATE types SET value='link' WHERE value='html';
--rollback UPDATE types SET value='html' WHERE value='link';