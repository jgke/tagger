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

import fi.jgke.tagger.domain.Source;
import fi.jgke.tagger.domain.Tag;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.Test;
import static org.junit.Assert.*;

public class TagTest {

    @Test
    public void testSetGetValue() {
        String value = UUID.randomUUID().toString();
        Tag instance = new Tag();
        instance.setValue(value);
        assertEquals(value, instance.getValue());
    }

    @Test
    public void testSetGetSources() {
        Tag instance = new Tag();
        List<Source> sources = new ArrayList<>();
        sources.add(new Source());
        instance.setSources(sources);
        assertEquals(sources, instance.getSources());
    }

    @Test
    public void testConstructorWithValue() {
        String value = UUID.randomUUID().toString();
        Tag instance = new Tag(value);
        assertEquals(value, instance.getValue());
    }

    @Test
    public void testEquals() {
        String value = UUID.randomUUID().toString();
        Tag instance = new Tag();
        instance.setValue(value);
        assertTrue(instance.equals(instance));
        assertFalse(instance.equals(null));
        assertFalse(instance.equals(this));
        Tag other = new Tag();
        other.setValue(UUID.randomUUID().toString());
        assertFalse(instance.equals(other));
        other.setValue(value);
        assertTrue(instance.equals(other));
    }

    @Test
    public void testHashcode() {
        String value = "test_tag";
        Tag instance = new Tag(value);
        assertEquals(-1146446698, instance.hashCode());
    }
}
