package fi.jgke.tagger.domain;

/**
 *
 * @author jgke
 */
public class SourceParameter {
    private String title;
    private String url;
    private Long sourcetype;

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

    public Long getSourcetype() {
        return sourcetype;
    }

    public void setSourcetype(Long sourcetype) {
        this.sourcetype = sourcetype;
    }

    @Override
    public String toString() {
        return "SourceParameter{" + "title=" + title + ", url=" + url + ", sourcetype=" + sourcetype + '}';
    }
    
    
}
