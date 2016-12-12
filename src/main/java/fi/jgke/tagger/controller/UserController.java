package fi.jgke.tagger.controller;

import fi.jgke.tagger.domain.Source;
import fi.jgke.tagger.domain.User;
import fi.jgke.tagger.repository.SourceRepository;
import fi.jgke.tagger.repository.TypeRepository;
import fi.jgke.tagger.repository.UserRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private SourceRepository sourceRepository;
    @Autowired
    private TypeRepository typeRepository;
    @Autowired
    private UserRepository userRepository;

    @RequestMapping
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @RequestMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        return userRepository.findOne(id);
    }

}
