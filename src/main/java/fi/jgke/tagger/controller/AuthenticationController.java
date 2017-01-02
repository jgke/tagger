package fi.jgke.tagger.controller;

import fi.jgke.tagger.domain.Person;
import fi.jgke.tagger.service.PersonService;
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

    @Autowired
    PersonService personService;

    @RequestMapping("/login")
    public String login(Model model, @RequestParam(required = false) String error,
            @RequestParam(required = false) String success) {
        if (error != null) {
            model.addAttribute("loginError", true);
        }
        if (success != null) {
            model.addAttribute("registerSuccess", true);
        }

        return "login";
    }

    @RequestMapping("/register")
    public String registerForm(Model model, @RequestParam(required = false) String duplicate) {
        if (duplicate != null) {
            model.addAttribute("registerDuplicate", true);
        }
        return "register";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(@RequestParam String username,
            @RequestParam String password) throws Exception {
        if (personRepository.findByUsername(username) != null) {
            return "redirect:/register?duplicate";
        }
        personService.registerPerson(username, password);
        return "redirect:/login?success";
    }
}
