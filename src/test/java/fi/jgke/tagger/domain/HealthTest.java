package fi.jgke.tagger.domain;

import java.util.UUID;
import org.junit.Test;
import static org.junit.Assert.*;

public class HealthTest {

    @Test
    public void testGetVersion() {
        String version = UUID.randomUUID().toString();
        Health instance = new Health(version, true);
        assertEquals(version, instance.getVersion());
        assertTrue(instance.isHealthy());
    }

}
