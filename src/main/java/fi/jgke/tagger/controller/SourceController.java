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

import fi.jgke.tagger.domain.Comment;
import fi.jgke.tagger.repository.SourceRepository;
import fi.jgke.tagger.domain.Source;
import fi.jgke.tagger.domain.Tag;
import fi.jgke.tagger.domain.Type;
import fi.jgke.tagger.exception.NotAuthorizedToDeleteSourceException;
import fi.jgke.tagger.repository.CommentRepository;
import fi.jgke.tagger.repository.TagRepository;
import fi.jgke.tagger.repository.TypeRepository;
import fi.jgke.tagger.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;

@Controller
@RequestMapping("/sources")
public class SourceController {

    @Autowired
    SourceRepository sourceRepository;

    @Autowired
    TypeRepository typeRepository;

    @Autowired
    TagRepository tagRepository;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    PersonService personService;

    @RequestMapping
    public String handleDefault(Model model, @PageableDefault Pageable pageable) {
        final PageRequest paged = new PageRequest(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Direction.DESC, "id"
        );
        Page<Source> sources = sourceRepository.findAll(paged);
        model.addAttribute("sources", sources);
        model.addAttribute("types", typeRepository.findAll());
        model.addAttribute("next", "page=" + (paged.getPageNumber() + 1) + "&limit=" + paged.getPageSize());
        model.addAttribute("prev", "page=" + (paged.getPageNumber() - 1) + "&limit=" + paged.getPageSize());

        return "sources";
    }

    @RequestMapping("/{id}")
    public String getSource(Model model, @PathVariable Long id) {
        Source source = sourceRepository.findOneOrThrow(id);
        model.addAttribute("source", source);
        model.addAttribute("type", source.getSourcetype().getValue());
        model.addAttribute("modifiable", personService.canCurrentUserModifySource(source));

        return "source";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String addSource(Model model, @RequestParam String title,
            @RequestParam String url, @RequestParam Long sourcetype) {

        if (!isValidUrl(url)) {
            model.addAttribute("error", "Invalid url format.");
            return handleDefault(model, new PageRequest(0, 10));
        }

        Type type = typeRepository.findOne(sourcetype);
        Source source = new Source();
        source.setTitle(title);
        source.setUrl(url);
        source.setSourcetype(type);
        source.setPerson(personService.getAuthenticatedPerson());

        source = sourceRepository.save(source);
        return "redirect:/sources/" + source.getId();
    }

    @RequestMapping(value = "/{id}/tags", method = RequestMethod.POST)
    public String addTag(Model model, @PathVariable Long id,
            @RequestParam String tagname) {
        tagname = tagname.trim().toLowerCase();
        if (tagname.length() > 32 || !tagname.matches("^[a-z0-9_-]+$")) {
            model.addAttribute("tagError", true);
            return getSource(model, id);
        }

        Source source = sourceRepository.findOneOrThrow(id);
        Tag tag = tagRepository.findByValueOrCreateNew(tagname);
        source.addTag(tag);
        sourceRepository.save(source);
        return "redirect:/sources/" + source.getId();
    }

    @RequestMapping(value = "/{id}/tags/{tagid}", method = RequestMethod.DELETE)
    public String deleteTag(@PathVariable Long id, @PathVariable Long tagid) {
        Source source = sourceRepository.findOneOrThrow(id);
        Tag tag = tagRepository.findOne(tagid);
        source.removeTag(tag);
        sourceRepository.save(source);
        return "redirect:/sources/" + source.getId();
    }

    @RequestMapping(value = "/{id}/comments", method = RequestMethod.POST)
    public String addComment(@PathVariable Long id, @RequestParam String body) {
        Source source = sourceRepository.findOneOrThrow(id);
        Comment comment = new Comment();
        comment.setComment(body);
        comment.setPerson(personService.getAuthenticatedPerson());
        comment.setSource(source);
        source.addComment(comment);
        commentRepository.save(comment);
        sourceRepository.save(source);
        return "redirect:/sources/" + source.getId();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public String deleteSource(@PathVariable Long id) {
        Source source = sourceRepository.findOneOrThrow(id);
        if (!personService.canCurrentUserModifySource(source)) {
            throw new NotAuthorizedToDeleteSourceException();
        }
        sourceRepository.delete(id);
        return "redirect:/sources";
    }

    private boolean isValidUrl(String url) {
        String[] schemes = {"http", "https"};
        UrlValidator urlValidator = new UrlValidator(schemes);
        return urlValidator.isValid(url);
    }

}
