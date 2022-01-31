package nl.stokpop.afterburner.metrics;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;


@Slf4j
@RequiredArgsConstructor
@Component
@ConditionalOnProperty(value = "management.metrics.export.prometheus.enabled", havingValue = "true")
public class PushStatistics {

    public static final Duration TIMEOUT_DURATION = Duration.ofMillis(800);
    public final CustomThroughputGauge customThroughputGauge;

    public final PushConfig config;

    private final HttpClient httpClient = HttpClient.newBuilder()
        .connectTimeout(TIMEOUT_DURATION)
        .build();

    private final OkHttpClient okHttpClient = new OkHttpClient.Builder()
        .connectTimeout(TIMEOUT_DURATION)
        .readTimeout(TIMEOUT_DURATION)
        .writeTimeout(TIMEOUT_DURATION).build();

    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    public enum RemotingType { J11HttpClient, OkHttpClient }

    @Value("afterburner.custom.metrics.remoting-type")
    private final RemotingType remotingType;

    @Scheduled(fixedDelay = 1000)
    void push() {
        if (remotingType == RemotingType.J11HttpClient) {
            sendMetric(customThroughputGauge.getRps());
        }
        else {
            sendMetricOkHttp(customThroughputGauge.getRps());
        }
    }

    private void sendMetric(long rps) {
        String totalUrl = config.getUrl() + "/v1/apps/" + config.getAppId() + "/metrics";
        try {
            String json = createJson(rps);

            URI envUri = new URI(totalUrl);
            HttpRequest httpRequest = HttpRequest.newBuilder(envUri)
                .header("Content-Type", "application/json")
                .header("Authorization", "Basic " + config.getBasicAuth())
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .timeout(TIMEOUT_DURATION).build();

            // this fails on certain jvm's, causing time out exceptions, maybe due to: https://bugs.openjdk.java.net/browse/JDK-8231449
            HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                log.error("Unexpected status code (not 200): " + response.statusCode() + " for " + httpRequest.uri() + " : " + response.body());
            }
        } catch (URISyntaxException | IOException | InterruptedException e) {
            log.error("Cannot post to " + totalUrl, e);
        }
    }

    private String createJson(long rps) throws JsonProcessingException {
        ArrayList<Metric> metricList = new ArrayList<>();
        metricList.add(new Metric("rps", rps, "rps"));
        Metrics metrics = new Metrics(config.getAppIndex(), metricList);
        String json = objectMapper.writeValueAsString(metrics);
        log.info("About to send: " + json);
        return json;
    }

    private void sendMetricOkHttp(long rps) {
        String totalUrl = config.getUrl() + "/v1/apps/" + config.getAppId() + "/metrics";
        try {
            String json = createJson(rps);

            RequestBody body = RequestBody.create(json, JSON);
            Request request = new Request.Builder()
                .url(totalUrl)
                .header("Authorization", "Basic " + config.getBasicAuth())
                .post(body)
                .build();
            try (Response response = okHttpClient.newCall(request).execute()) {
                if (response.code() != 200) {
                    log.error("Unexpected status code (not 200): " + response.code() + " for " + request.url() + " : " + response.body());
                }
            }
        } catch (IOException e) {
            log.error("Cannot post to " + totalUrl, e);
        }
    }
}
