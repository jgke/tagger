--liquibase formatted sql
--changeset jgke:3

-- Unique tag values means destruction.
DELETE FROM tags_sources;
DELETE FROM tags;

ALTER TABLE types ADD CONSTRAINT types_has_unique_value UNIQUE (value);
--rollback ALTER TABLE types DROP CONSTRAINT types_has_unique_value;

ALTER TABLE tags ADD CONSTRAINT tags_has_unique_value UNIQUE (value);
--rollback ALTER TABLE tags DROP CONSTRAINT tags_has_unique_value;

ALTER TABLE tags_sources ADD CONSTRAINT tags_sources_delete_on_deleted_source
    FOREIGN KEY(source) REFERENCES sources(id)
    ON DELETE CASCADE;
--rollback ALTER TABLE tags_sources DROP CONSTRAINT delete_without_source;

ALTER TABLE tags_sources ADD CONSTRAINT tags_sources_is_unique UNIQUE (tag, source);
--rollback ALTER TABLE tags_sources DROP CONSTRAINT tags_sources_is_unique;

ALTER TABLE tags_sources ADD CONSTRAINT tags_sources_delete_on_deleted_tag
    FOREIGN KEY(tag) REFERENCES tags(id)
    ON DELETE CASCADE;
--rollback ALTER TABLE tags_sources DROP CONSTRAINT delete_without_tag;
