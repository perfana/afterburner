package nl.stokpop.afterburner;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "afterburner")
@Data
public class AfterburnerProperties {

    String name = "afterburner-one";
    String databaseConnectQuery = "SELECT 1";

    int asyncMaxPoolSize = 10;
    int asyncCorePoolSize = 5;
    int asyncQueueSize = -1;

}
