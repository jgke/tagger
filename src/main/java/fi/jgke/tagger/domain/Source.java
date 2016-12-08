package fi.jgke.tagger.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * A single image, URL or such thing.
 *
 * @author jgke
 */
@Entity
@Table(name = "source")
public class Source extends AbstractPersistable<Long> {

    @Column(name = "title")
    private String title;
    @Column(name = "url")
    private String url;

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

}
