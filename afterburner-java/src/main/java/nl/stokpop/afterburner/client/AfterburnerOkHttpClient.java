package nl.stokpop.afterburner.client;

import okhttp3.OkHttpClient;

import java.time.Duration;

public class AfterburnerOkHttpClient implements AfterburnerClient {

    private OkHttpClient okHttpClient;

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
