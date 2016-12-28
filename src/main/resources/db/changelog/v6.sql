--liquibase formatted sql
--changeset jgke:6

ALTER TABLE sources ALTER COLUMN sourcetype DROP DEFAULT;
ALTER SEQUENCE sources_sourcetype_seq OWNED BY NONE;
DROP SEQUENCE sources_sourcetype_seq;
--rollback ALTER TABLE source ALTER COLUMN sourcetype TYPE SERIAL;

ALTER TABLE tags_sources ALTER COLUMN source DROP DEFAULT;
ALTER SEQUENCE tags_sources_source_seq OWNED BY NONE;
DROP SEQUENCE tags_sources_source_seq;
--rollback ALTER TABLE tags_sources ALTER COLUMN source TYPE SERIAL;

ALTER TABLE tags_sources ALTER COLUMN tag DROP DEFAULT;
ALTER SEQUENCE tags_sources_tag_seq OWNED BY NONE;
DROP SEQUENCE tags_sources_tag_seq;
--rollback ALTER TABLE tags_sources ALTER COLUMN tag TYPE SERIAL;

DROP TABLE comments;