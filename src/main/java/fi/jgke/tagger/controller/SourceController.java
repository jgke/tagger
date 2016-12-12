package fi.jgke.tagger.controller;

import fi.jgke.tagger.domain.Source;
import fi.jgke.tagger.domain.SourceParameter;
import fi.jgke.tagger.domain.Type;
import fi.jgke.tagger.repository.SourceRepository;
import fi.jgke.tagger.repository.TypeRepository;
import fi.jgke.tagger.repository.UserRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/sources")
public class SourceController {

    @Autowired
    private SourceRepository sourceRepository;
    @Autowired
    private TypeRepository typeRepository;
    @Autowired
    private UserRepository userRepository;

    @RequestMapping
    public List<Source> getSources() {
        return sourceRepository.findAll();
    }

    @RequestMapping("/{id}")
    public Source getSources(@PathVariable Long id) {
        return sourceRepository.findOne(id);
    }

    @RequestMapping(method = RequestMethod.POST)
    public Source addSource(@RequestBody SourceParameter receivedSource) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        
        Type type = typeRepository.findOne(receivedSource.getSourcetype());
        User user = userRepository.findByUsername(username);
        
        Source source = new Source();
        source.setTitle(receivedSource.getTitle());
        source.setUrl(receivedSource.getUrl());
        source.setSourcetype(type);
        source.setSubmitter(user);
        
        return sourceRepository.save(source);
    }

}
