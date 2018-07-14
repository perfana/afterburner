package nl.stokpop.afterburner.client;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Component("remote-call-okhttp-client")
public class RemoteCallOkHttp implements RemoteCall {

    private final static Logger log = LoggerFactory.getLogger(RemoteCallOkHttp.class);

    private final String baseUrl;
    private final Map<String, String> headers;

    private final OkHttpClient okHttpClient;

    public RemoteCallOkHttp(String baseUrl, Map<String, String> headers, OkHttpClient okHttpClient) {
            this.okHttpClient = okHttpClient;
            this.baseUrl = baseUrl;
            this.headers = new HashMap<>(headers);
            log.info("Created RemoteCallOkHttp with baseUrl [{}] headers [{}] okHttpClient [{}]",
                    baseUrl, headers, okHttpClient);
        }

    @Autowired
    public RemoteCallOkHttp(
            @Value("${afterburner.remote.call.base_url:http://localhost:8080}") final String baseUrl) {
            this(baseUrl, Collections.emptyMap());
        }

    public RemoteCallOkHttp(final String baseUrl, final Map<String, String> headers) {
            this(baseUrl, headers, new OkHttpClient());
        }

    @Override
    public String call(final String path) throws RemoteCallException {
        final String completeUrl = RemoteCallUtil.createCompleteUrl(baseUrl, path);
        log.info("Remote call via OkHttp [{}]", completeUrl);
        try {
            String result = get(completeUrl);
            log.debug("Result: [{}]", result);
            return result;
        } catch (IOException e) {
            throw new RuntimeException(String.format("Failed to call url [%s]", completeUrl), e);
        }
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
