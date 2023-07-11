package io.perfana.afterburner;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "afterburner")
@Data
public class AfterburnerProperties {

    @Value("${spring.application.name}")
    String name = "Afterburner";
    String databaseConnectQuery = "SELECT 1";

    // simulate the default Spring executor by not using the custom
    // executor
    boolean isCustomExecutorEnabled = true;

    int asyncMaxPoolSize = 10;
    int asyncCorePoolSize = 5;
    int asyncQueueSize = -1;
    int asyncKeepAliveSeconds = 60;
    int delayCallLimit = 10;
}
