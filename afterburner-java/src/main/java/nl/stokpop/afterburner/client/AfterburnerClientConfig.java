package nl.stokpop.afterburner.client;

import feign.Feign;
import feign.Retryer;
import feign.httpclient.ApacheHttpClient;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.binder.httpcomponents.PoolingHttpClientConnectionManagerMetricsBinder;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AfterburnerClientConfig {
    
    @Bean
    public CloseableHttpClient feignHttpClient() {

        RequestConfig config = RequestConfig.custom()
                .setConnectionRequestTimeout(250)
                .setConnectTimeout(1)
                .setSocketTimeout(1)
                .build();

        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setDefaultMaxPerRoute(1);
        connectionManager.setMaxTotal(1);

        PoolingHttpClientConnectionManagerMetricsBinder metrics =
            new PoolingHttpClientConnectionManagerMetricsBinder(connectionManager, "afterburner-http-client");
        metrics.bindTo(Metrics.globalRegistry);

        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create()
                .setConnectionManager(connectionManager)
                .setDefaultRequestConfig(config)
                .disableConnectionState();

        return httpClientBuilder.build();
    }

    @Bean
    public Feign.Builder feignBuilder(CloseableHttpClient client) {
        return Feign.builder()
                .retryer(Retryer.NEVER_RETRY)
                .client(new ApacheHttpClient(client));
    }
}
