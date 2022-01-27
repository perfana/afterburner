package nl.stokpop.afterburner.metrics;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;

@Slf4j
@RequiredArgsConstructor
class RequestStatistics {

    public final CustomThroughputGauge customThroughputGauge;

    @Scheduled(fixedDelay = 1000)
    void measure() {
        customThroughputGauge.setRps();
    }
}