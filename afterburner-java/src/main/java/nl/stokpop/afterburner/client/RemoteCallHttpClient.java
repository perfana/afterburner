package nl.stokpop.afterburner.client;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Component("remote-call-http-client")
public class RemoteCallHttpClient implements RemoteCall {

    private final static Logger log = LoggerFactory.getLogger(RemoteCallHttpClient.class);

    private final static Charset CHARSET_UTF_8 = StandardCharsets.UTF_8;

    private final String baseUrl;
    private final Map<String, String> additionalHeaders;

    private final CloseableHttpClient httpClient;
    
    @Autowired
    public RemoteCallHttpClient(
            CloseableHttpClient httpClient,
            @Value("${afterburner.remote.call.base_url:http://localhost:8080}") String baseUrl,
            Map<String, String> additionalHeaders) {
        this.httpClient = httpClient;
        this.baseUrl = baseUrl;
        this.additionalHeaders = new HashMap<>(additionalHeaders);
        log.info("Created RemoteCallHttpClient with baseUrl [{}] additionalHeaders [{}] httpClient [{}]",
                baseUrl, additionalHeaders, httpClient);
    }
    
    @Override
    public String call(final String path) throws RemoteCallException {
        final String completeUrl = RemoteCallUtil.createCompleteUrl(this.baseUrl, path);

        log.info("Remote call via HttpClient [{}]", completeUrl);

        HttpGet httpGet = new HttpGet(completeUrl);
        additionalHeaders.forEach(httpGet::addHeader);
        try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
            String result = EntityUtils.toString(response.getEntity(), CHARSET_UTF_8);
            log.debug("Result: [{}]", result);
            return result;
        } catch (IOException ex) {
            String message = String.format("Failed to call url [%s]", completeUrl);
            throw new RemoteCallException(message, ex);
        }
    }

}
