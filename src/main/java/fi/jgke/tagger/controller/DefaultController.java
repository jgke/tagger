package fi.jgke.tagger.controller;

import fi.jgke.tagger.repository.SourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class DefaultController {

    @Autowired
    SourceRepository sourceRepository;

    @RequestMapping("/")
    public String handleDefault(Model model) {
        model.addAttribute("sources", sourceRepository.findAll());
        return "index";
    }

}
