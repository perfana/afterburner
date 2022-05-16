package nl.stokpop.afterburner.client;

import feign.Feign;
import feign.Retryer;
import feign.httpclient.ApacheHttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
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

        return HttpClientBuilder.create()
                        .setDefaultRequestConfig(config)
                        .setMaxConnPerRoute(1)
                        .setMaxConnTotal(1)
                        .disableConnectionState()
                        .build();
    }

    @Bean
    public Feign.Builder feignBuilder(CloseableHttpClient client) {
        return Feign.builder()
                .retryer(Retryer.NEVER_RETRY)
                .client(new ApacheHttpClient(client));
    }
}
