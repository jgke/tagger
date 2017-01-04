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
package fi.jgke.tagger.unit.controller;

import fi.jgke.tagger.controller.FrontPageController;
import fi.jgke.tagger.controller.SourceController;
import fi.jgke.tagger.domain.Source;
import fi.jgke.tagger.domain.Tag;
import fi.jgke.tagger.domain.Type;
import fi.jgke.tagger.repository.PersonRepository;
import fi.jgke.tagger.repository.SourceRepository;
import fi.jgke.tagger.repository.TagRepository;
import fi.jgke.tagger.repository.TypeRepository;
import fi.jgke.tagger.service.PersonService;
import fi.jgke.tagger.service.SourceService;
import fi.jgke.tagger.service.TagService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.mockito.Mockito.*;

public class SourceControllerTest {

    @InjectMocks
    SourceController sourceController;

    @Mock
    SourceRepository sourceRepository;

    @Mock
    TagRepository tagRepository;

    @Mock
    PersonService personService;

    @Mock
    TagService tagService;

    @Mock
    Model model;

    @Mock
    Source source;

    @Before
    public void init() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getSource() throws Exception {
        Source source = new Source();
        String type = "test_type";
        source.setSourcetype(new Type() {
            {
                setValue(type);
            }
        });
        Long id = 5L;
        when(sourceRepository.findOneOrThrow(id)).thenReturn(source);
        when(personService.canCurrentUserModifySource(source)).thenReturn(false);
        String result = sourceController.getSource(model, id, null, null);
        Assert.assertEquals("source", result);
        verify(model, never()).addAttribute(eq("tagError"), any());
        verify(model, never()).addAttribute(eq("duplicateTagError"), any());
        verify(model).addAttribute("source", source);
        verify(model).addAttribute("type", type);
        verify(model).addAttribute("modifiable", false);
    }

    @Test
    public void getSourceWithTagError() throws Exception {
        Source source = new Source();
        String type = "test_type";
        source.setSourcetype(new Type() {
            {
                setValue(type);
            }
        });
        Long id = 5L;
        when(sourceRepository.findOneOrThrow(id)).thenReturn(source);
        String result = sourceController.getSource(model, id, "true", null);
        Assert.assertEquals("source", result);
        verify(model).addAttribute(eq("tagError"), any());
    }

    @Test
    public void getSourceWithDuplicateTag() throws Exception {
        Source source = new Source();
        String type = "test_type";
        source.setSourcetype(new Type() {
            {
                setValue(type);
            }
        });
        Long id = 5L;
        when(sourceRepository.findOneOrThrow(id)).thenReturn(source);
        String result = sourceController.getSource(model, id, null, "true");
        Assert.assertEquals("source", result);
        verify(model).addAttribute(eq("duplicateTagError"), any());
    }

    @Test
    public void addTag() throws Exception {
        String tagname = "foo";
        Tag tag = new Tag(tagname);
        Long id = 3L;
        when(sourceRepository.findOneOrThrow(id)).thenReturn(source);
        when(tagRepository.findByValueOrCreateNew(tagname)).thenReturn(tag);
        when(tagService.isValidTag(tagname)).thenReturn(true);
        when(source.getTags()).thenReturn(new HashSet<>());
        when(source.getId()).thenReturn(id);
        String result = sourceController.addTag(id, tagname);
        Assert.assertEquals("redirect:/sources/" + id, result);
        verify(source).addTag(tag);
        verify(sourceRepository).save(source);
    }

    @Test
    public void addBadTags() throws Exception {
        String tag = "bad tag";
        Long id = 4L;
        when(source.getId()).thenReturn(id);
        when(tagService.isValidTag(tag)).thenReturn(false);
        String result = sourceController.addTag(id, tag);
        Assert.assertEquals("redirect:/sources/" + id + "?badtag", result);
    }

    @Test
    public void addDuplicateTag() throws Exception {
        String tagname = "foo";
        Tag tag = new Tag(tagname);
        Set<Tag> tags = new HashSet<>();
        tags.add(tag);
        Long id = 3L;
        when(sourceRepository.findOneOrThrow(id)).thenReturn(source);
        when(tagRepository.findByValueOrCreateNew(tagname)).thenReturn(tag);
        when(tagService.isValidTag(tagname)).thenReturn(true);
        when(source.getId()).thenReturn(id);
        when(source.getTags()).thenReturn(tags);
        String result = sourceController.addTag(id, tagname);
        Assert.assertEquals("redirect:/sources/" + id + "?duplicatetag", result);
    }
}
