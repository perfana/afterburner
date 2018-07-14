package nl.stokpop.afterburner.client;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class RemoteCallHttpClientTest {

    @Test
    public void createCompleteUrl() {
        assertNull(RemoteCallUtil.createCompleteUrl(null, null));
        assertEquals("base", RemoteCallUtil.createCompleteUrl("base", null));
        assertEquals("path", RemoteCallUtil.createCompleteUrl(null, "path"));
        assertEquals("base/path", RemoteCallUtil.createCompleteUrl("base", "path"));
        assertEquals("base/path", RemoteCallUtil.createCompleteUrl("base", "/path"));
        assertEquals("base/path", RemoteCallUtil.createCompleteUrl(" base ", " /path"));
        assertEquals("base/path", RemoteCallUtil.createCompleteUrl(" base ", " /path"));
    }
}