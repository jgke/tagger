/*
 * Copyright 2016 Jaakko Hannikainen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package fi.jgke.tagger.controller;

import fi.jgke.tagger.repository.SourceRepository;
import fi.jgke.tagger.domain.Source;
import fi.jgke.tagger.domain.Tag;
import fi.jgke.tagger.domain.Type;
import fi.jgke.tagger.exception.InvalidSourceUrlException;
import fi.jgke.tagger.exception.TagAlreadyExistsException;
import fi.jgke.tagger.repository.TagRepository;
import fi.jgke.tagger.repository.TypeRepository;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.apache.commons.validator.routines.UrlValidator;

@Controller
@RequestMapping("/sources")
public class SourceController {

    @Autowired
    SourceRepository sourceRepository;

    @Autowired
    TypeRepository typeRepository;

    @Autowired
    TagRepository tagRepository;

    @RequestMapping
    public String handleDefault(Model model) {
        model.addAttribute("sources", sourceRepository.findAll());
        model.addAttribute("types", typeRepository.findAll());
        return "sources";
    }

    @RequestMapping("/{id}")
    public String getSource(Model model, @PathVariable Long id) {
        Source source = sourceRepository.findOne(id);
        model.addAttribute("source", source);
        model.addAttribute("type", source.getSourcetype().getValue());
        return "source";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String addSource(@RequestParam String title, @RequestParam String url,
            @RequestParam Long sourcetype) {

        if (!isValidUrl(url)) {
            throw new InvalidSourceUrlException();
        }

        Type type = typeRepository.findOne(sourcetype);
        Source source = new Source();
        source.setTitle(title);
        source.setUrl(url);
        source.setSourcetype(type);
        source = sourceRepository.save(source);
        return "redirect:/sources/" + source.getId();
    }

    @RequestMapping(value = "/{id}/tags", method = RequestMethod.POST)
    @Transactional
    public String addTag(@PathVariable Long id, @RequestParam String tagname) {
        Source source = sourceRepository.findOne(id);
        Tag tag = tagRepository.findByValueOrCreateNew(tagname);
        source.addTag(tag);
        sourceRepository.save(source);
        return "redirect:/sources/" + source.getId();
    }

    private boolean isValidUrl(String url) {
        String[] schemes = {"http", "https"};
        UrlValidator urlValidator = new UrlValidator(schemes);
        return urlValidator.isValid(url);
    }

}
