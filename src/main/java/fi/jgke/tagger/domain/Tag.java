package fi.jgke.tagger.domain;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * A type for sources.
 *
 * @author jgke
 */
@Entity
@Table(name = "tags")
public class Tag extends AbstractPersistable<Long> {

    @Column(name = "value")
    private String value;

    @ManyToMany(mappedBy = "tags", cascade = CascadeType.ALL)
    List<Source> sources;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
