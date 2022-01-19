package nl.stokpop.afterburner.metrics;

import io.micrometer.prometheus.PrometheusMeterRegistry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@ConditionalOnProperty(value = "management.metrics.export.prometheus.enabled", matchIfMissing = true)
@EnableScheduling
class PerformanceMetricsConfiguration {
    @Bean
    RequestStatistics requestStatistics(final CustomThroughputGauge customThroughputGauge) {
        return new RequestStatistics(customThroughputGauge);
    }

    @Bean(initMethod = "init")
    CustomThroughputGauge customThroughputGauge(final PrometheusMeterRegistry meterRegistry) {
        return new CustomThroughputGauge(meterRegistry);
    }
}