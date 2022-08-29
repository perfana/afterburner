package io.perfana.afterburner.metrics;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;

@Slf4j
@RequiredArgsConstructor
class RequestStatistics {

    public final CustomThroughputGauge customThroughputGauge;

    @Scheduled(fixedDelayString = "${app.statistics.throughput.schedule:1000}")
    void measure() {
        customThroughputGauge.setRps();
    }
}