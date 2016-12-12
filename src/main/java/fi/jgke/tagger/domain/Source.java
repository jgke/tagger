package fi.jgke.tagger.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
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
    @JoinColumn(name = "submitter")
    private User submitter;

    @OneToOne
    @JoinColumn(name = "sourcetype")
    private Type sourcetype;

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

    public User getSubmitter() {
        return submitter;
    }

    public void setSubmitter(User submitter) {
        this.submitter = submitter;
    }

    public Type getSourcetype() {
        return sourcetype;
    }

    public void setSourcetype(Type sourcetype) {
        this.sourcetype = sourcetype;
    }

}
