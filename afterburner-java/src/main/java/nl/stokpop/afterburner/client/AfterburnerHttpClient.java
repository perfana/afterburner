package nl.stokpop.afterburner.client;

import org.apache.http.client.HttpClient;

import java.time.Duration;

public class AfterburnerHttpClient implements AfterburnerClient {

    private HttpClient httpClient;

    @Override
    public String remote(final String path) {
        return "not implemented";
    }

    @Override
    public String delay(final Duration duration) {
        return "not implemented";
    }

    @Override
    public String memoryGrow(final int objects, final int elements) {
        return "not implemented";
    }

    @Override
    public String memoryGrow() {
        return "not implemented";
    }

    @Override
    public String calculateIdentityMatrix(final int size) {
        return "not implemented";
    }

    @Override
    public String calculateIdentityMatrix() {
        return "not implemented";
    }
}
