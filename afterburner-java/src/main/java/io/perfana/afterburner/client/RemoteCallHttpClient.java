package io.perfana.afterburner.client;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component("remote-call-http-client")
public class RemoteCallHttpClient implements RemoteCall {

    private final static Charset CHARSET_UTF_8 = StandardCharsets.UTF_8;

    private final AfterburnerConfig afterburnerConfig;

    private final CloseableHttpClient httpClient;
    
    @Autowired
    public RemoteCallHttpClient(
            CloseableHttpClient httpClient,
            AfterburnerConfig config) {
        this.httpClient = httpClient;
        this.afterburnerConfig = config;
        log.info("Created RemoteCallHttpClient with baseUrl [{}] additionalHeaders [{}] httpClient [{}]",
                config.getBaseUrl(), config.getAdditionalHeaders(), httpClient);
    }
    
    @Override
    public String call(final String path) throws RemoteCallException, IOException {
        final String completeUrl = RemoteCallUtil.createCompleteUrl(afterburnerConfig.getBaseUrl(), path);

        log.info("Remote call via HttpClient [{}]", completeUrl);

        HttpGet httpGet = new HttpGet(completeUrl);
        afterburnerConfig.getAdditionalHeaders().forEach(httpGet::addHeader);
        try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
            String result = EntityUtils.toString(response.getEntity(), CHARSET_UTF_8);
            log.debug("Result: [{}]", result);
            return result;
        }
    }

}
