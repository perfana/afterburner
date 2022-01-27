package nl.stokpop.afterburner.metrics;

import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import org.junit.jupiter.api.Test;

class PushStatisticsTest {

    @Test
    public void testPush() {
        PrometheusConfig config = key -> null;
        String user = System.getenv("AFTB_CF_USER");
        String password = System.getenv("AFTB_CF_PASSWORD");
        String url = System.getenv("AFTB_CF_URL");
        String appId = System.getenv("AFTB_CF_APP_ID");
        PushConfig pushConfig = new PushConfig(url,
            user,
            password,
            PushConfig.encodeBasicAuth(user, password),
            0,
            appId
        );
        CustomThroughputGauge customThroughputGauge = new CustomThroughputGauge(new PrometheusMeterRegistry(config));
        PushStatistics pushStatistics = new PushStatistics(customThroughputGauge, pushConfig);
        pushStatistics.push();
    }

}