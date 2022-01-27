package nl.stokpop.afterburner.metrics;

import io.micrometer.prometheus.PrometheusMeterRegistry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@ConditionalOnProperty(value = "management.metrics.export.prometheus.enabled", havingValue = "true")
@Configuration
@EnableScheduling
class PerformanceMetricsConfiguration {

    @Bean
    PushConfig pushConfig() {
        return PushConfig.fromCfEnv();
    }

    @Bean
    RequestStatistics requestStatistics(final CustomThroughputGauge customThroughputGauge) {
        return new RequestStatistics(customThroughputGauge);
    }

    @Bean(initMethod = "init")
    CustomThroughputGauge customThroughputGauge(final PrometheusMeterRegistry meterRegistry) {
        return new CustomThroughputGauge(meterRegistry);
    }
}