--liquibase formatted sql
--changeset jgke:5

ALTER TABLE sources
    ADD thumbnail varchar(40);
--rollback ALTER TABLE sources DROP COLUMN thumbnail;
