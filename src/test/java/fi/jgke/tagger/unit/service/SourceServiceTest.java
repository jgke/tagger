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

import fi.jgke.tagger.domain.Comment;
import fi.jgke.tagger.domain.Person;
import fi.jgke.tagger.domain.Source;
import fi.jgke.tagger.domain.Type;
import fi.jgke.tagger.repository.CommentRepository;
import fi.jgke.tagger.repository.SourceRepository;
import fi.jgke.tagger.service.CommentService;
import fi.jgke.tagger.service.PersonService;
import fi.jgke.tagger.service.SourceService;
import fi.jgke.tagger.service.ThumbnailService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

import java.util.ArrayList;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SourceServiceTest {

    @InjectMocks
    SourceService sourceService;

    @Mock
    SourceRepository sourceRepository;

    @Mock
    PersonService personService;

    @Mock
    ThumbnailService thumbnailService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void createSource() {
        Person person = new Person();
        String title = "title";
        String url = "http://example.com";
        Type type = new Type();

        ArgumentCaptor<Source> varArgs = ArgumentCaptor.forClass(Source.class);
        when(sourceRepository.save(varArgs.capture())).thenAnswer(AdditionalAnswers.returnsFirstArg());
        when(personService.getAuthenticatedPerson()).thenReturn(person);

        Source result = sourceService.createSource(title, url, type);

        verify(sourceRepository).save(result);
        Assert.assertEquals(title, result.getTitle());
        Assert.assertEquals(url, result.getUrl());
        Assert.assertEquals(type, result.getSourcetype());
        Assert.assertEquals(person, result.getPerson());
        verify(thumbnailService).createThumbnailForSource(result);
    }


    @Test
    public void isValidUrl() {
        String validUrls[] = {"http://example.com", "https://example.com", "http://xn--bcher-kva.ch", "http://1.2.3.4/"};
        String invalidUrls[] = {"", "http://", "ftp://example.com"};

        for (String s : validUrls) {
            assertTrue(sourceService.isValidUrl(s));
        }
        for (String s : invalidUrls) {
            assertTrue(!sourceService.isValidUrl(s));
        }
    }
}
