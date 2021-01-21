package nl.stokpop.afterburner.client;

import brave.http.HttpTracing;
import brave.httpclient.TracingHttpClientBuilder;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class AfterburnerConfig {

    @Value("${afterburner.remote.call.httpclient.connect.timeout.millis:5000}")
    private int connectTimeoutMillis;

    @Value("${afterburner.remote.call.httpclient.socket.timeout.millis:5000}")
    private int socketTimeoutMillis;

    @Value("${afterburner.remote.call.httpclient.connection.request.timeout.millis:400}")
    private int connectionRequestTimeoutMillis;

    @Value("${afterburner.remote.call.httpclient.connections.max:20}")
    private int connectionsMax;

    // {:} is empty map, which is default if additional header not present
    @Value("#{${afterburner.remote.call.additional.headers:{:}}}")
    Map<String, String> additionalHeaders;

    // Need to inject traceHttpClientBuilder to have spans in http headers
    @Bean
    public CloseableHttpClient createHttpClient(@Qualifier("traceHttpClientBuilder") HttpClientBuilder builder) {

        RequestConfig defaultRequestConfig = RequestConfig.custom()
                .setConnectTimeout(connectTimeoutMillis)
                .setSocketTimeout(socketTimeoutMillis)
                .setConnectionRequestTimeout(connectionRequestTimeoutMillis)
                .build();

        return builder
                .setDefaultRequestConfig(defaultRequestConfig)
                .setMaxConnPerRoute(connectionsMax)
                .setMaxConnTotal(connectionsMax)
                .build();
    }

    // WORKAROUND: should be present automatically: traceHttpClientBuilder, via org.springframework.cloud.sleuth.autoconfig.brave.instrument.web.client.BraveWebClientAutoConfiguration
    // but for some reason not?
    @Bean
    HttpClientBuilder traceHttpClientBuilder(HttpTracing httpTracing) {
        return TracingHttpClientBuilder.create(httpTracing);
    }

    @Bean(name = "additionalHttpHeaders")
    public Map<String, String> additionalHttpHeaders() {
        return additionalHeaders;
    }

}

