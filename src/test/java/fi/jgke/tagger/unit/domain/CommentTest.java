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
package fi.jgke.tagger.unit.domain;

import fi.jgke.tagger.domain.Comment;
import fi.jgke.tagger.domain.Person;
import fi.jgke.tagger.domain.Source;
import java.util.UUID;
import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.*;

public class CommentTest {

    @Test
    public void testDefaultConstructor() {
        Comment comment = new Comment();
    }

    @Test
    public void testConstructor() {
        String value = UUID.randomUUID().toString();
        Person person = new Person();
        Source source = new Source();
        Comment comment = new Comment(value, person, source);
        Assert.assertEquals(value, comment.getComment());
        Assert.assertEquals(person, comment.getPerson());
        Assert.assertEquals(source, comment.getSource());
    }

    @Test
    public void testSetGetComment() {
        String value = UUID.randomUUID().toString();
        Comment instance = new Comment();
        instance.setComment(value);
        assertEquals(value, instance.getComment());
    }

    @Test
    public void testSetGetPerson() {
        String value = UUID.randomUUID().toString();
        Person person = new Person();
        person.setUsername(value);
        Comment instance = new Comment();
        instance.setPerson(person);
        assertEquals(person, instance.getPerson());
    }

    @Test
    public void testSetGetSource() {
        String value = UUID.randomUUID().toString();
        Source source = new Source();
        source.setTitle(value);
        Comment instance = new Comment();
        instance.setSource(source);
        assertEquals(source, instance.getSource());
    }

}
