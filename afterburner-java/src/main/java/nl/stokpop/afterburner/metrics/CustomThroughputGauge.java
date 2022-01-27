package nl.stokpop.afterburner.metrics;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import java.util.Collections;
import java.util.concurrent.atomic.AtomicLong;

import static io.prometheus.client.Collector.Type.SUMMARY;

@ConditionalOnProperty(value = "management.metrics.export.prometheus.enabled", havingValue = "true")
@RequiredArgsConstructor
class CustomThroughputGauge {

    static final String METRIC = "http_server_requests_seconds";
    static final String COUNT = "http_server_requests_seconds_count";
    static final String PATH = "/delay"; //Choose on which endpoint you want to measure requests per second.

    private final AtomicLong totalRequestCount = new AtomicLong();
    private final AtomicLong processedRequestCount = new AtomicLong();
    private final AtomicLong rps = new AtomicLong();

    private final PrometheusMeterRegistry meterRegistry;

    void init() {
        meterRegistry.gauge("custom_http_throughput_on_delay", rps); //name of your custom metric
    }

    void setRps() {
        totalRequestCount.set(getCurrentRequestCount());
        rps.set(calculateRPS());
        processedRequestCount.set(totalRequestCount.get());
    }

    public long getRps() {
        return rps.get();
    }

    private long calculateRPS() {
        return totalRequestCount.get() - processedRequestCount.get();
    }

    public long getCurrentRequestCount() {
        return Collections.list(meterRegistry.getPrometheusRegistry().metricFamilySamples()).stream()
                .filter(samples -> METRIC.equals(samples.name))
                .filter(samples -> SUMMARY == samples.type)
                .flatMap(samples -> samples.samples.stream())
                .filter(sample -> COUNT.equals(sample.name))
                .filter(sample -> sample.labelValues.stream().anyMatch(label -> label.startsWith(PATH)))
                .mapToLong(sample -> (long) sample.value)
                .sum();
    }
}