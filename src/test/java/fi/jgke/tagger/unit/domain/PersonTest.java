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

import fi.jgke.tagger.domain.Person;
import fi.jgke.tagger.domain.Source;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import junit.framework.Assert;
import org.junit.Test;
import static org.junit.Assert.*;
import org.springframework.security.crypto.bcrypt.BCrypt;

public class PersonTest {

    public PersonTest() {
    }

    @Test
    public void testSetGetUsername() {
        String value = UUID.randomUUID().toString();
        Person instance = new Person();
        instance.setUsername(value);
        assertEquals(value, instance.getUsername());
    }

    @Test
    public void testSetGetPassword() {
        String value = UUID.randomUUID().toString();
        Person instance = new Person();
        instance.setPassword(value);
        assertEquals(BCrypt.hashpw(value, instance.getSalt()), instance.getPassword());
    }

    @Test
    public void testSetGetSalt() {
        String value = UUID.randomUUID().toString();
        Person instance = new Person();
        instance.setSalt(value);
        assertEquals(value, instance.getSalt());
    }

    @Test
    public void testSetGetIsAdmin() {
        boolean value;
        Person instance = new Person();

        value = false;
        instance.setAdmin(value);
        assertEquals(value, instance.isAdmin());

        value = true;
        instance.setAdmin(value);
        assertEquals(value, instance.isAdmin());
    }

    @Test
    public void testSetGetSources() {
        Person instance = new Person();
        List<Source> sources = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Source source = new Source();
            source.setTitle(UUID.randomUUID().toString());
            sources.add(source);
        }
        instance.setSources(sources);
        assertEquals(sources, instance.getSources());
    }
}
