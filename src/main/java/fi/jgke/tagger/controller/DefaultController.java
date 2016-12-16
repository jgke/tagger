package fi.jgke.tagger.controller;

import fi.jgke.tagger.repository.SourceRepository;
import fi.jgke.tagger.domain.Source;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
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

    @RequestMapping("/{id}")
    public String getSource(Model model, @PathVariable Long id) {
        Source source = sourceRepository.findOne(id);
        model.addAttribute("type", source.getSourcetype().getValue());
        model.addAttribute("title", source.getTitle());
        model.addAttribute("tags", source.getTags());
        model.addAttribute("url", source.getUrl());
        return "source";
    }

}
