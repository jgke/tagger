/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.jgke.tagger.domain;

import java.util.UUID;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author jgke
 */
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
}
