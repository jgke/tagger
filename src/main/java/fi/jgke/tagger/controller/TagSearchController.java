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
import fi.jgke.tagger.domain.Tag;
import fi.jgke.tagger.exception.TagNotFoundException;
import fi.jgke.tagger.repository.TagRepository;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/search")
public class TagSearchController {

    @Autowired
    SourceRepository sourceRepository;

    @Autowired
    TagRepository tagRepository;

    @RequestMapping
    public String getSource(Model model, @RequestParam String tagstring) {
        Tag tag = tagRepository.findByValueOrThrow(tagstring);
        model.addAttribute("tag", tag.getValue());
        model.addAttribute("sources", tag.getSources());
        return "tagsearch";
    }

}
