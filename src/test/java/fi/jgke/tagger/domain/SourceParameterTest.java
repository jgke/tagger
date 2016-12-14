package fi.jgke.tagger.domain;

import java.util.UUID;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author jgke
 */
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
