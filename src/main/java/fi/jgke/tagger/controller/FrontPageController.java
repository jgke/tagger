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

import fi.jgke.tagger.domain.Source;
import fi.jgke.tagger.domain.Type;
import fi.jgke.tagger.repository.SourceRepository;
import fi.jgke.tagger.repository.TypeRepository;
import fi.jgke.tagger.service.PersonService;
import fi.jgke.tagger.service.SourceService;
import fi.jgke.tagger.service.ThumbnailService;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

@Controller
@RequestMapping("/sources")
public class FrontPageController {

    @Autowired
    SourceRepository sourceRepository;

    @Autowired
    SourceService sourceService;

    @Autowired
    TypeRepository typeRepository;

    @RequestMapping
    public String handleDefault(Model model, @PageableDefault Pageable pageable,
                                @RequestParam(required = false) String badurl) {
        final PageRequest paged = new PageRequest(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Direction.DESC, "id"
        );

        if (badurl != null) {
            model.addAttribute("error", "Invalid url format.");
        }

        Page<Source> sources = sourceRepository.findAll(paged);
        model.addAttribute("sources", sources);
        model.addAttribute("types", typeRepository.findAll());
        model.addAttribute("next", "page=" + (paged.getPageNumber() + 1) + "&limit=" + paged.getPageSize());
        model.addAttribute("prev", "page=" + (paged.getPageNumber() - 1) + "&limit=" + paged.getPageSize());

        return "sources";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String addSource(@RequestParam String title,
                            @RequestParam String url, @RequestParam Long sourcetype) throws IOException {

        if (!sourceService.isValidUrl(url)) {
            return "redirect:/sources?badurl";
        }

        Type type = typeRepository.findOne(sourcetype);
        Source source = sourceService.createSource(title, url, type);
        return "redirect:/sources/" + source.getId();
    }
}
