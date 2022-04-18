package nl.stokpop.afterburner.client;

import brave.http.HttpTracing;
import brave.httpclient.TracingHttpClientBuilder;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.binder.httpcomponents.MicrometerHttpClientInterceptor;
import io.micrometer.core.instrument.binder.httpcomponents.PoolingHttpClientConnectionManagerMetricsBinder;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import lombok.Getter;
import okhttp3.OkHttpClient;
import org.apache.http.HttpRequest;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Collections;
import java.util.Map;

@Configuration
@Getter
public class AfterburnerConfig {

    @Value("${afterburner.remote.call.httpclient.connect.timeout.millis:5000}")
    private int connectTimeoutMillis;

    @Value("${afterburner.remote.call.httpclient.socket.timeout.millis:5000}")
    private int socketTimeoutMillis;

    @Value("${afterburner.remote.call.httpclient.connection.request.timeout.millis:400}")
    private int connectionRequestTimeoutMillis;

    @Value("${afterburner.remote.call.httpclient.connections.max:20}")
    private int connectionsMax;

    @Value("${afterburner.remote.call.base_url:http://localhost:8080}")
    private String baseUrl;

    // {:} is empty map, which is default if additional header not present
    @Value("#{${afterburner.remote.call.additional.headers:{:}}}")
    private Map<String, String> additionalHeaders;

    // Need to inject traceHttpClientBuilder to have spans in http headers
    @Bean
    public CloseableHttpClient createHttpClient(@Qualifier("traceHttpClientBuilder") HttpClientBuilder builder) {

        RequestConfig defaultRequestConfig = RequestConfig.custom()
                .setConnectTimeout(connectTimeoutMillis)
                .setSocketTimeout(socketTimeoutMillis)
                .setConnectionRequestTimeout(connectionRequestTimeoutMillis)
                .build();

        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setDefaultMaxPerRoute(connectionsMax);
        connectionManager.setMaxTotal(connectionsMax);

        MicrometerHttpClientInterceptor interceptor = new MicrometerHttpClientInterceptor(Metrics.globalRegistry,
                this::extractUriWithoutParamsAsString,
                Tags.empty(),
                true);

        CloseableHttpClient httpClient = builder
            .setConnectionManager(connectionManager)
            .setDefaultRequestConfig(defaultRequestConfig)
            .addInterceptorFirst(interceptor.getRequestInterceptor())
            .addInterceptorLast(interceptor.getResponseInterceptor())
            .build();

        String ipAddress = RemoteCallUtil.getIpAddress(baseUrl);
        PoolingHttpClientConnectionManagerMetricsBinder metrics =
            new PoolingHttpClientConnectionManagerMetricsBinder(connectionManager, "afterburner-http-client", Tags.of("IP", ipAddress == null ? "unknown" : ipAddress));
        metrics.bindTo(Metrics.globalRegistry);

        return httpClient;
    }

    private String extractUriWithoutParamsAsString(HttpRequest request) {
        String uri = request.getRequestLine().getUri();
        URI realUri = UriComponentsBuilder.fromUriString(uri).replaceQuery(null).build(Collections.emptyMap());
        return String.valueOf(realUri);
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

    @Bean
    public OkHttpClient okHttpClient() {
        return new OkHttpClient();
    }

    @Bean(destroyMethod = "shutdown")
    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(3);
        threadPoolTaskScheduler.setThreadNamePrefix("AfterburnerTaskScheduler-");
        return threadPoolTaskScheduler;
    }

    // needed to reactivate the jvm metrics after registering executor (???)
    // tip from stackoverflow below, but that is about security
    // https://stackoverflow.com/questions/57607445/spring-actuator-jvm-metrics-not-showing-when-globalmethodsecurity-is-enabled
    @Bean
    InitializingBean forcePrometheusPostProcessor(BeanPostProcessor meterRegistryPostProcessor, PrometheusMeterRegistry registry) {
        return () -> meterRegistryPostProcessor.postProcessAfterInitialization(registry, "");
    }

}

