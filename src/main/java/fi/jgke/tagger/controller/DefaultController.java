package fi.jgke.tagger.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class DefaultController {

    @RequestMapping("*")
    @ResponseBody
    public String handleDefault() {
        return "Hello world!";
    }
}
