package fi.jgke.tagger.controller;

import fi.jgke.tagger.domain.Health;
import javax.annotation.PostConstruct;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/health")
public class HealthController {

    private String version;
    
    @PostConstruct
    public void init() {
        this.version = System.getenv().getOrDefault("HEROKU_RELEASE_VERSION", "");
    }
    
    @RequestMapping
    public HttpEntity<Health> health() {

        Health health = new Health(version, true);
        health.add(linkTo(methodOn(HealthController.class).health()).withSelfRel());

        return new ResponseEntity<>(health, HttpStatus.OK);
    }
    
}
