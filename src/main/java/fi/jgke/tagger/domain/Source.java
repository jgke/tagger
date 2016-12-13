package fi.jgke.tagger.domain;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * A single image, URL or such thing.
 *
 * @author jgke
 */
@Entity
@Table(name = "sources")
public class Source extends AbstractPersistable<Long> {

    @Column(name = "title")
    private String title;
    @Column(name = "url")
    private String url;

    @OneToOne
    @JoinColumn(name = "sourcetype")
    private Type sourcetype;

    @ManyToMany
    @JoinTable(
            name = "tags_sources",
            joinColumns = @JoinColumn(name = "source", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "tag", referencedColumnName = "id"))
    List<Tag> tags;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Type getSourcetype() {
        return sourcetype;
    }

    public void setSourcetype(Type sourcetype) {
        this.sourcetype = sourcetype;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }
}
