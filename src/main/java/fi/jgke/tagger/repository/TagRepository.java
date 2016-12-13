package fi.jgke.tagger.repository;

import fi.jgke.tagger.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {
    Tag findByValue(String value);
}
