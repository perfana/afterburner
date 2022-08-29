package io.perfana.afterburner.client;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

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