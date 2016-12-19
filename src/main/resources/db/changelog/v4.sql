--liquibase formatted sql
--changeset jgke:4

ALTER TABLE tags_sources DROP CONSTRAINT tags_sources_source_fkey;
--rollback ALTER TABLE tags_sources ADD CONSTRAINT tags_sources_source_fkey FOREIGN KEY (source) REFERENCES sources(id);

ALTER TABLE tags_sources DROP CONSTRAINT tags_sources_tag_fkey;
--rollback ALTER TABLE tags_sources ADD CONSTRAINT tags_sources_tag_fkey FOREIGN KEY (tag) REFERENCES tags(id);
