package fi.jgke.tagger.repository;

import fi.jgke.tagger.domain.Type;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TypeRepository extends JpaRepository<Type, Long> {
    Type findByValue(String value);
}
