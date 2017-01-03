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
import fi.jgke.tagger.domain.Source;
import fi.jgke.tagger.repository.SourceRepository;
import fi.jgke.tagger.repository.TypeRepository;
import fi.jgke.tagger.service.PersonService;
import fi.jgke.tagger.service.ThumbnailService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;

import java.util.UUID;

import static org.mockito.Mockito.*;

public class FrontPageControllerTest {
    @InjectMocks
    FrontPageController frontPageController;

    @Mock
    SourceRepository sourceRepository;

    @Mock
    TypeRepository typeRepository;

    @Mock
    PersonService personService;

    @Mock
    ThumbnailService thumbnailService;

    @Mock
    Model model;

    @Before
    public void init() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void handleDefault() throws Exception {
        int page = 0;
        int size = 10;
        Pageable pageable = new PageRequest(page, size);
        String result = frontPageController.handleDefault(model, pageable, null);
        Assert.assertEquals("sources", result);
        verify(model, never()).addAttribute(eq("error"), any(Object.class));
    }

    @Test
    public void handleDefaultWithError() throws Exception {
        int page = 0;
        int size = 10;
        Pageable pageable = new PageRequest(page, size);
        String result = frontPageController.handleDefault(model, pageable, "yes");
        verify(model).addAttribute(eq("error"), any(String.class));
    }


    @Test
    public void addSourceWithGoodUrl() throws Exception {
        String title = UUID.randomUUID().toString();
        String url = "http://example.com";
        Long sourceType = 1L;
        Long id = 5L;
        when(sourceRepository.save(any(Source.class))).thenReturn(new Source() {{setId(id);}});
        String result = frontPageController.addSource(title, url, sourceType);
        verify(sourceRepository).save(any(Source.class));
        Assert.assertEquals("redirect:/sources/" + id, result);
    }

    @Test
    public void addSourceWithBadUrl() throws Exception {
        String title = UUID.randomUUID().toString();
        String url = "bad url";
        Long sourceType = 1L;
        String result = frontPageController.addSource(title, url, sourceType);
        verify(sourceRepository, never()).save(any(Source.class));
        Assert.assertEquals("redirect:/sources?badurl", result);
    }
}