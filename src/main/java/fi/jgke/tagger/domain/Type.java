package fi.jgke.tagger.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * A type for sources.
 *
 * @author jgke
 */
@Entity
@Table(name = "types")
public class Type extends AbstractPersistable<Long> {

    @Column(name = "value")
    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
