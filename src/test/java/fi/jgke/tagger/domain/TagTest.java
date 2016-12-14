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
public class TagTest {
   

    @Test
    public void testSetGetValue() {
        String value = UUID.randomUUID().toString();
        Tag instance = new Tag();
        instance.setValue(value);
        assertEquals(value, instance.getValue());
    }
    
}
