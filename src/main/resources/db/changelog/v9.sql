--liquibase formatted sql
--changeset jgke:9

ALTER TABLE persons
    ADD isadmin boolean DEFAULT false;
--rollback ALTER TABLE persons DROP COLUMN isadmin;
