package nl.stokpop.afterburner.client;

import java.time.Duration;

/**
 * Use afterburner clients to call the afterburner endpoints
 * from Java.
 */
public interface AfterburnerClient {
    /**
     * Call the path (part of url) on remote Afterburner.
     * @param path the path part of the url, starting with /
     * @return the reply from remote call
     */
    String remote(String path) throws AfterburnerClientException;
    String delay(Duration duration) throws AfterburnerClientException;
    String memoryGrow(int objects, int elements) throws AfterburnerClientException;
    String memoryGrow() throws AfterburnerClientException;
    String calculateIdentityMatrix(int size) throws AfterburnerClientException;
    String calculateIdentityMatrix() throws AfterburnerClientException;
}
