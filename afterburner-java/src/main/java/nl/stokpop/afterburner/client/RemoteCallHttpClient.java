package nl.stokpop.afterburner.client;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component("remote-call-http-client")
public class RemoteCallHttpClient implements RemoteCall {

    private final static Logger log = LoggerFactory.getLogger(RemoteCallHttpClient.class);

    private final static Charset CHARSET_UTF_8 = Charset.forName("UTF-8");

    private final String baseUrl;
    private final Map<String, String> headers;

    private final CloseableHttpClient httpClient;

    public RemoteCallHttpClient(String baseUrl, Map<String, String> headers, CloseableHttpClient httpClient) {
        this.httpClient = httpClient;
        this.baseUrl = baseUrl;
        this.headers = new HashMap<>(headers);
        log.info("Created RemoteCallHttpClient with baseUrl [{}] headers [{}] httpClient [{}]",
                baseUrl, headers, httpClient);
    }

    @Autowired
    public RemoteCallHttpClient(
            @Value("${afterburner.remote.call.base_url:http://localhost:8080}") final String baseUrl) {
        this(baseUrl, Collections.emptyMap());
    }

    public RemoteCallHttpClient(final String baseUrl, final Map<String, String> headers) {
        this(baseUrl, headers, createHttpClient());
    }

    private static CloseableHttpClient createHttpClient() {
        int connectTimeoutMillis = (int) TimeUnit.SECONDS.toMillis(5);
        int socketTimeoutMillis = (int) TimeUnit.SECONDS.toMillis(5);
        int connectionRequestTimeoutMillis = 400;

        RequestConfig defaultRequestConfig = RequestConfig.custom()
                .setConnectTimeout(connectTimeoutMillis)
                .setSocketTimeout(socketTimeoutMillis)
                .setConnectionRequestTimeout(connectionRequestTimeoutMillis)
                .build();

        int maxConnections = 20;

        return HttpClients.custom()
                .setDefaultRequestConfig(defaultRequestConfig)
                .setMaxConnPerRoute(maxConnections)
                .setMaxConnTotal(maxConnections)
                .build();
    }

    @Override
    public String call(final String path) throws RemoteCallException {
        final String completeUrl = RemoteCallUtil.createCompleteUrl(this.baseUrl, path);
        log.info("Remote call via HttpClient [{}]", completeUrl);
        HttpGet httpGet = new HttpGet(completeUrl);

        try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
            String result = EntityUtils.toString(response.getEntity(), CHARSET_UTF_8);
            log.debug("Result: [{}]", result);
            return result;
        } catch (IOException e) {
            throw new RemoteCallException(String.format("Failed to call url [%s]", completeUrl), e);
        }
    }

}
