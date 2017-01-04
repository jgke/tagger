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
package fi.jgke.tagger.unit.repository;

import fi.jgke.tagger.domain.Source;
import fi.jgke.tagger.exception.SourceNotFoundException;
import fi.jgke.tagger.repository.SourceRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.when;

public class SourceRepositoryTest {
// Wake me up when Mockito 2 works with spring
    /*
    @Mock
    SourceRepository sourceRepository;

    @Before
    public void init() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void findOneWorks() throws Exception {
        Long id = 5L;
        Source source = new Source();
        when(sourceRepository.findOne(id)).thenReturn(source);
        when(sourceRepository.findOneOrThrow(id)).thenCallRealMethod();
        Source result = sourceRepository.findOneOrThrow(id);
        Assert.assertEquals(source, result);
    }

    @Test(expected = SourceNotFoundException.class)
    public void findOneThrows() throws Exception {
        Long id = 5L;
        when(sourceRepository.findOne(id)).thenReturn(null);
        when(sourceRepository.findOneOrThrow(id)).thenCallRealMethod();
        sourceRepository.findOneOrThrow(id);
    }
*/
}
