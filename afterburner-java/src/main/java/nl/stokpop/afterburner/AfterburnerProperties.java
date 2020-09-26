package nl.stokpop.afterburner;

import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "afterburner")
@ConstructorBinding
@Value
public class AfterburnerProperties {

    String name = "afterburner-one";
    String databaseConnectQuery = "SELECT 1";

    int asyncMaxPoolSize = 10;
    int asyncCorePoolSize = 5;
    int asyncQueueSize = -1;

}
