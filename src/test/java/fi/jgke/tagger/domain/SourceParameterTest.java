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

import java.util.UUID;
import org.junit.Test;
import static org.junit.Assert.*;

public class SourceParameterTest {

    @Test
    public void testSetGetTitle() {
        String title = UUID.randomUUID().toString();
        SourceParameter instance = new SourceParameter();
        instance.setTitle(title);
        assertEquals(title, instance.getTitle());
    }

    @Test
    public void testSetGetUrl() {
        String url = UUID.randomUUID().toString();
        SourceParameter instance = new SourceParameter();
        instance.setUrl(url);
        assertEquals(url, instance.getUrl());
    }

    @Test
    public void testSetGetTitleAndUrl() {
        String title = UUID.randomUUID().toString();
        String url = UUID.randomUUID().toString();
        SourceParameter instance = new SourceParameter();
        instance.setTitle(title);
        instance.setUrl(url);
        assertEquals(title, instance.getTitle());
        assertEquals(url, instance.getUrl());
    }

    @Test
    public void testSetGetSourceParametertype() {
        Long sourcetype = UUID.randomUUID().getLeastSignificantBits();
        SourceParameter instance = new SourceParameter();
        instance.setSourcetype(sourcetype);
        assertEquals(sourcetype, instance.getSourcetype());
    }

}
