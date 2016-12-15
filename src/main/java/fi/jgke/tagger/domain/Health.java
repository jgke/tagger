package fi.jgke.tagger.domain;

import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Health extends ResourceSupport {

    private final String version;
    private final boolean healthy;

    @JsonCreator
    public Health(@JsonProperty("version") String version, @JsonProperty("healthy") boolean healthy) {
        this.version = version;
        this.healthy = healthy;
    }

    public String getVersion() {
        return version;
    }

    public boolean isHealthy() {
        return healthy;
    }
}
