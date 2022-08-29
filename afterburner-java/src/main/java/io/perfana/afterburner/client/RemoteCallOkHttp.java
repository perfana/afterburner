package io.perfana.afterburner.client;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component("remote-call-okhttp-client")
public class RemoteCallOkHttp implements RemoteCall {

    private final static Logger log = LoggerFactory.getLogger(RemoteCallOkHttp.class);

    private AfterburnerConfig afterburnerConfig;

    private final OkHttpClient okHttpClient;

    @Autowired
    public RemoteCallOkHttp(AfterburnerConfig config, OkHttpClient okHttpClient) {
        this.okHttpClient = okHttpClient;
        this.afterburnerConfig = config;
        log.info("Created RemoteCallOkHttp with baseUrl [{}] headers [{}] okHttpClient [{}]",
                afterburnerConfig.getBaseUrl(), afterburnerConfig.getAdditionalHeaders(), okHttpClient);
    }

    @Override
    public String call(final String path) throws RemoteCallException, IOException {
        final String completeUrl = RemoteCallUtil.createCompleteUrl(afterburnerConfig.getBaseUrl(), path);
        log.info("Remote call via OkHttp [{}]", completeUrl);
        String result = get(completeUrl);
        log.debug("Result: [{}]", result);
        return result;
    }

    private String get(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        try (Response response = okHttpClient.newCall(request).execute()) {
            ResponseBody responseBody = response.body();
            return responseBody == null ? "null" : responseBody.string();
        }
    }
}
