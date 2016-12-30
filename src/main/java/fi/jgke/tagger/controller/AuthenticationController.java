package fi.jgke.tagger.controller;

import fi.jgke.tagger.domain.Person;
import fi.jgke.tagger.exception.UsernameAlreadyExistsException;
import fi.jgke.tagger.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthenticationController {

    @Autowired
    PersonRepository personRepository;

    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    @RequestMapping("/loginerror")
    public String loginerror(Model model) {
        model.addAttribute("loginError", true);
        return "login";
    }

    @RequestMapping("/register")
    public String register_form() {
        return "register";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(@RequestParam String username,
            @RequestParam String password) throws Exception {
        if (personRepository.findByUsername(username) != null) {
            throw new UsernameAlreadyExistsException();
        }
        Person person = new Person();
        person.setUsername(username);
        person.setPassword(password);
        personRepository.save(person);
        return "redirect:/";
    }
}
