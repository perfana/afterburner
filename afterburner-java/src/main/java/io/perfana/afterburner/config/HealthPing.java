package io.perfana.afterburner.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class HealthPing implements HealthIndicator {

    private final long startTime = System.currentTimeMillis();

    @Override
    public Health getHealth(boolean includeDetails) {
        return HealthIndicator.super.getHealth(includeDetails);
    }

    @Override
    public Health health() {
        long uptime = System.currentTimeMillis() - startTime;
        boolean isReady = uptime > 20_000;
        log.info("Health check is ready: {}", isReady);
        if (isReady) {
            return Health.up().build();
        } else {
            return Health.outOfService().build();
        }
    }
}
