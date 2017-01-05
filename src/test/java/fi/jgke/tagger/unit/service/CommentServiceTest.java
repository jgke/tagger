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
import fi.jgke.tagger.domain.Tag;
import fi.jgke.tagger.repository.CommentRepository;
import fi.jgke.tagger.repository.PersonRepository;
import fi.jgke.tagger.repository.SourceRepository;
import fi.jgke.tagger.repository.TagRepository;
import fi.jgke.tagger.service.CommentService;
import fi.jgke.tagger.service.PersonService;
import fi.jgke.tagger.service.TagService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CommentServiceTest {

    @InjectMocks
    CommentService commentService;

    @Mock
    SourceRepository sourceRepository;

    @Mock
    CommentRepository commentRepository;

    @Mock
    PersonService personService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void addComment() {
        Person person = new Person();
        String comment = "foo";
        Source source = new Source();
        source.setComments(new ArrayList<>());

        ArgumentCaptor<Comment> varArgs = ArgumentCaptor.forClass(Comment.class);
        when(commentRepository.save(varArgs.capture())).thenAnswer(AdditionalAnswers.returnsFirstArg());
        when(personService.getAuthenticatedPerson()).thenReturn(person);

        commentService.addComment(comment, source);

        verify(sourceRepository).save(source);
        Assert.assertEquals(comment, varArgs.getValue().getComment());
        Assert.assertEquals(comment, source.getComments().get(0).getComment());
    }
}
