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
package fi.jgke.tagger.domain;

import fi.jgke.tagger.exception.TagAlreadyExistsException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.Test;
import static org.junit.Assert.*;

public class SourceTest {

    public SourceTest() {
    }

    @Test
    public void testSetGetTitle() {
        String title = UUID.randomUUID().toString();
        Source instance = new Source();
        instance.setTitle(title);
        assertEquals(title, instance.getTitle());
    }

    @Test
    public void testSetGetUrl() {
        String url = UUID.randomUUID().toString();
        Source instance = new Source();
        instance.setUrl(url);
        assertEquals(url, instance.getUrl());
    }

    @Test
    public void testSetGetTitleAndUrl() {
        String title = UUID.randomUUID().toString();
        String url = UUID.randomUUID().toString();
        Source instance = new Source();
        instance.setTitle(title);
        instance.setUrl(url);
        assertEquals(title, instance.getTitle());
        assertEquals(url, instance.getUrl());
    }

    @Test
    public void testSetGetSourcetype() {
        Type sourcetype = new Type();
        sourcetype.setValue(UUID.randomUUID().toString());
        Source instance = new Source();
        instance.setSourcetype(sourcetype);
        assertEquals(sourcetype, instance.getSourcetype());
    }

    @Test
    public void testSetGetTags() throws NoSuchMethodException {
        List<Tag> tags = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Tag tag = new Tag();
            tag.setValue(UUID.randomUUID().toString());
            if (!tags.contains(tag)) {
                tags.add(tag);
            }
        }
        Source instance = new Source();
        instance.setTags(tags);
        assertEquals(tags, instance.getTags());
    }

    @Test
    public void testAddTag() {
        Source instance = new Source();
        Tag tag = new Tag("foo");
        instance.addTag(tag);
        assertTrue(instance.getTags().contains(tag));
    }

    @Test(expected=TagAlreadyExistsException.class)
    public void testAddDuplicateTagThrows() {
        Source instance = new Source();
        Tag tag = new Tag("foo");
        instance.addTag(tag);
        instance.addTag(tag);
    }
}
