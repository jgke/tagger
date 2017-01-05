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
package fi.jgke.tagger.unit.service;

import fi.jgke.tagger.domain.Source;
import fi.jgke.tagger.domain.Tag;
import fi.jgke.tagger.repository.SourceRepository;
import fi.jgke.tagger.repository.TagRepository;
import fi.jgke.tagger.service.TagService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

public class TagServiceTest {

    @InjectMocks
    TagService tagService;

    @Mock
    SourceRepository sourceRepository;

    @Mock
    TagRepository tagRepository;

    @Mock
    Model model;

    List<Source> sources;
    List<Tag> tags;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        sources = new ArrayList<>();
        tags = new ArrayList<>();

        tags.add(new Tag("foo"));
        tags.add(new Tag("bar"));
        tags.add(new Tag("baz"));
        tags.add(new Tag("qux"));

        Source s;

        s = new Source();
        s.addTag(tags.get(0));
        s.addTag(tags.get(1));
        sources.add(s);
        tags.get(0).addSource(s);
        tags.get(1).addSource(s);

        s = new Source();
        s.addTag(tags.get(1));
        s.addTag(tags.get(2));
        sources.add(s);
        tags.get(1).addSource(s);
        tags.get(2).addSource(s);

        s = new Source();
        s.addTag(tags.get(0));
        s.addTag(tags.get(2));
        sources.add(s);
        tags.get(0).addSource(s);
        tags.get(2).addSource(s);
    }

    @Test
    public void testIsValidTag() {
        String validTags[] = {"tag", "dashes_and-underscores", "numbers_1234567890",
                "abcdefghijklmnopqrstuvwxyz", "12345678901234567890123456789012"};
        String invalidTags[] = {"", "和製漢語", "123456789012345678901234567890123", " ", "Uppercase", "!"};

        for (String s : validTags) {
            assertTrue(tagService.isValidTag(s));
        }
        for (String s : invalidTags) {
            assertTrue(!tagService.isValidTag(s));
        }
    }

    private class SearchPair {
        public String search;
        public List<Source> sources;

        public SearchPair(String s, List<Source> ss) {
            search = s;
            sources = ss;
        }
    }

    @Test
    public void testSearch() {
        for (Tag tag : tags) {
            when(tagRepository.findByValue(tag.getValue())).thenReturn(tag);
        }
        when(sourceRepository.findAll()).thenReturn(sources);
        SearchPair[] pairs = new SearchPair[]{
                new SearchPair("", sources),
                new SearchPair("not_tag", sources),
                new SearchPair("foo bar baz qux", new ArrayList<>()),
                new SearchPair("foo", sources.stream().filter((t) -> t.getTags().contains(new Tag("foo")))
                        .collect(Collectors.toList())),
                new SearchPair("foo not_tag", sources.stream().filter((t) -> t.getTags().contains(new Tag("foo")))
                        .collect(Collectors.toList())),
                new SearchPair("bar", sources.stream().filter((t) -> t.getTags().contains(new Tag("bar")))
                        .collect(Collectors.toList())),
                new SearchPair("qux", new ArrayList<>()),
                new SearchPair("foo !bar", new ArrayList<Source>() {{
                    add(sources.get(2));
                }})
        };
        for (SearchPair pair : pairs) {
            List<Source> result = tagService.searchSources(pair.search);
            Assert.assertEquals(pair.sources, result);
        }
    }
}
