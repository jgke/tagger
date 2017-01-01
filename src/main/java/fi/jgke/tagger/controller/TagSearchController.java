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
import fi.jgke.tagger.repository.SourceRepository;
import fi.jgke.tagger.domain.Tag;
import fi.jgke.tagger.repository.TagRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
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

    private Set<Tag> getTagsFromList(Stream<String> tags) {
        return tags
                .map((t) -> tagRepository.findByValue(t))
                .filter((t) -> t != null)
                .collect(Collectors.toSet());
    }

    private Stream<Source> removeSourcesContainingTags(Stream<Source> sources,
            Set<Tag> tags) {
        return sources.filter((s) -> s.getTags().stream()
                .allMatch((t) -> !tags.contains(t)));
    }

    private Stream<Source> removeSourcesNotContainingTags(Stream<Source> sources,
            Set<Tag> tags) {
            return sources.filter((s) -> s.getTags().containsAll(tags));
    }

    private Stream<Source> getSourcesFromTags(Set<Tag> tags) {
        if (tags.isEmpty()) {
            return sourceRepository.findAll().stream();
        }
        return tags.stream().findAny().get().getSources().stream();
    }

    private Set<Source> getSourcesFromTags(Set<Tag> tags, Set<Tag> nottags) {
        Stream<Source> sources;
        sources = getSourcesFromTags(tags);
        sources = removeSourcesNotContainingTags(sources, tags);
        sources = removeSourcesContainingTags(sources, nottags);
        return sources.collect(Collectors.toSet());
    }

    @RequestMapping
    public String getSource(Model model, @RequestParam String tagstring) {
        List<String> strings = Arrays.asList(tagstring.split(" "));
        Stream<String> tagstrings = strings.stream()
                .filter((t) -> !t.startsWith("!"));
        Stream<String> nottagstrings = strings.stream()
                .filter((t) -> t.startsWith("!"))
                .map((t) -> t.substring(1));

        Set<Tag> tags = getTagsFromList(tagstrings);
        Set<Tag> nottags = getTagsFromList(nottagstrings);
        Set<Source> filtered_sources = getSourcesFromTags(tags, nottags);

        model.addAttribute("searchquery", tagstring);
        model.addAttribute("sources", new ArrayList<>(filtered_sources));
        return "tagsearch";
    }

}
