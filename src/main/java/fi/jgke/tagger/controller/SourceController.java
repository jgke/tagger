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
import fi.jgke.tagger.service.ThumbnailService;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/sources/{id}")
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

    @Autowired
    ThumbnailService thumbnailService;

    @RequestMapping(method = RequestMethod.GET)
    public String getSource(Model model, @PathVariable Long id,
                            @RequestParam(required = false) String badtag,
                            @RequestParam(required = false) String duplicatetag) {
        Source source = sourceRepository.findOneOrThrow(id);
        if (badtag != null) {
            model.addAttribute("tagError", true);
        }
        if (duplicatetag != null) {
            model.addAttribute("duplicateTagError", true);
        }

        model.addAttribute("source", source);
        model.addAttribute("type", source.getSourcetype().getValue());
        model.addAttribute("modifiable", personService.canCurrentUserModifySource(source));

        return "source";
    }

    @RequestMapping(value = "/tags", method = RequestMethod.POST)
    public String addTag(@PathVariable Long id, @RequestParam String tagname) {
        tagname = tagname.trim().toLowerCase();
        if (tagname.length() > 32 || !tagname.matches("^[a-z0-9_-]+$")) {
            return "redirect:/sources/" + id + "/?badtag";
        }

        Source source = sourceRepository.findOneOrThrow(id);
        Tag tag = tagRepository.findByValueOrCreateNew(tagname);

        if (source.getTags().contains(tag)) {
            return "redirect:/sources/" + id + "/?duplicatetag";
        }

        source.addTag(tag);
        sourceRepository.save(source);
        return "redirect:/sources/" + source.getId();
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public String deleteSource(@PathVariable Long id) {
        Source source = sourceRepository.findOneOrThrow(id);
        if (!personService.canCurrentUserModifySource(source)) {
            throw new NotAuthorizedToDeleteSourceException();
        }
        sourceRepository.delete(id);
        return "redirect:/sources";
    }

    @RequestMapping(value = "/tags/{tagid}", method = RequestMethod.DELETE)
    public String deleteTag(@PathVariable Long id, @PathVariable Long tagid) {
        Source source = sourceRepository.findOneOrThrow(id);
        Tag tag = tagRepository.findOne(tagid);
        source.removeTag(tag);
        sourceRepository.save(source);
        return "redirect:/sources/" + source.getId();
    }

    @RequestMapping(value = "/comments", method = RequestMethod.POST)
    public String addComment(@PathVariable Long id, @RequestParam String body) {
        Source source = sourceRepository.findOneOrThrow(id);
        Comment comment = new Comment(body, personService.getAuthenticatedPerson(),
                source);
        source.addComment(comment);
        commentRepository.save(comment);
        sourceRepository.save(source);
        return "redirect:/sources/" + source.getId();
    }


    @RequestMapping("/thumbnail")
    @ResponseBody
    public byte[] getThumbnail(@PathVariable Long id) {
        Source source = sourceRepository.findOneOrThrow(id);
        return source.getThumbnail();
    }

    private boolean isValidUrl(String url) {
        String[] schemes = {"http", "https"};
        UrlValidator urlValidator = new UrlValidator(schemes);
        return urlValidator.isValid(url);
    }

}
