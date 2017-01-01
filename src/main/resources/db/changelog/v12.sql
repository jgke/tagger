--liquibase formatted sql
--changeset jgke:12

ALTER TABLE sources DROP COLUMN thumbnail;
--rollback ALTER TABLE sources ADD thumbnail varchar(40);

ALTER TABLE sources
    ADD thumbnail bytea;
--rollback ALTER TABLE sources DROP COLUMN thumbnail;