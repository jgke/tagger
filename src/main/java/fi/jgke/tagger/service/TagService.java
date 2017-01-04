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
package fi.jgke.tagger.service;

import fi.jgke.tagger.domain.Source;
import fi.jgke.tagger.repository.SourceRepository;
import fi.jgke.tagger.domain.Tag;
import fi.jgke.tagger.repository.TagRepository;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TagService {

    @Autowired
    SourceRepository sourceRepository;

    @Autowired
    TagRepository tagRepository;

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

    private List<Source> searchSources(Set<Tag> tags, Set<Tag> nottags) {
        Stream<Source> sources;
        sources = getSourcesFromTags(tags);
        sources = removeSourcesNotContainingTags(sources, tags);
        sources = removeSourcesContainingTags(sources, nottags);
        return sources.collect(Collectors.toList());
    }

    private Set<Tag> getTagsFromList(Stream<String> tags) {
        return tags
                .map((t) -> tagRepository.findByValue(t))
                .filter((t) -> t != null)
                .collect(Collectors.toSet());
    }

    public List<Source> searchSources(String tagstring) {
        List<String> strings = Arrays.asList(tagstring.split(" "));
        Stream<String> tagstrings = strings.stream()
                .filter((t) -> !t.startsWith("!"));
        Stream<String> nottagstrings = strings.stream()
                .filter((t) -> t.startsWith("!"))
                .map((t) -> t.substring(1));
        Set<Tag> tags = getTagsFromList(tagstrings);
        Set<Tag> nottags = getTagsFromList(nottagstrings);

        return searchSources(tags, nottags);
    }

    public boolean isValidTag(String tag) {
        if (tag.length() > 32)
            return false;
        if(!tag.matches("^[a-z0-9_-]+$"))
            return false;
        return true;
    }
}
