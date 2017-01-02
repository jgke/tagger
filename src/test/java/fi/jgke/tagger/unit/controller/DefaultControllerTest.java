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

import fi.jgke.tagger.controller.DefaultController;
import fi.jgke.tagger.domain.Source;
import fi.jgke.tagger.repository.SourceRepository;
import fi.jgke.tagger.service.ThumbnailService;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class DefaultControllerTest {

    public DefaultControllerTest() {
    }

    @InjectMocks
    DefaultController defaultController;

    @Mock
    SourceRepository sourceRepository;

    @Mock
    ThumbnailService thumbnailService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testHandleDefault() {
        assertEquals("redirect:/sources", defaultController.handleDefault());
    }

    @Test
    public void testRecreateAllThumbnails() {
        List<Source> sources = new ArrayList<>();
        sources.add(new Source());
        sources.add(new Source());

        Mockito.when(sourceRepository.findAll()).thenReturn(sources);
        String result = defaultController.recreateAllThumbnails();
        assertEquals("redirect:/", result);
        for (Source s : sources) {
            Mockito.verify(thumbnailService).createThumbnailForSource(s);
            Mockito.verify(sourceRepository).save(s);
        }
    }

}
