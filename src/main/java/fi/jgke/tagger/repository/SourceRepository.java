package fi.jgke.tagger.repository;

import fi.jgke.tagger.domain.Source;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SourceRepository extends JpaRepository<Source, Long> {

    Source findByTitle(String title);
}
