package nl.stokpop.afterburner.metrics;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.concurrent.atomic.AtomicLong;

import static io.prometheus.client.Collector.Type.SUMMARY;

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
        totalRequestCount.set(getCurrentRequestCount().longValue());
        rps.set(calculateRPS().longValue());
        processedRequestCount.set(totalRequestCount.get());
    }

    private AtomicLong calculateRPS() {
        return new AtomicLong(totalRequestCount.get() - processedRequestCount.get());
    }

    public AtomicLong getCurrentRequestCount() {
        return new AtomicLong(Collections.list(meterRegistry.getPrometheusRegistry().metricFamilySamples()).stream()
                .filter(samples -> METRIC.equals(samples.name))
                .filter(samples -> SUMMARY == samples.type)
                .flatMap(samples -> samples.samples.stream())
                .filter(sample -> COUNT.equals(sample.name))
                .filter(sample -> sample.labelValues.stream().anyMatch(label -> label.startsWith(PATH)))
                .mapToLong(sample -> (long) sample.value)
                .sum());
    }
}