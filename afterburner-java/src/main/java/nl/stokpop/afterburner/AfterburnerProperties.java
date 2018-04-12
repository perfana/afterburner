package nl.stokpop.afterburner;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AfterburnerProperties {

    private String afterburnerName;

    public AfterburnerProperties(
            @Value(value = "${afterburner.name:Simply-Anonymous}") final String afterburnerName) {
        this.afterburnerName = afterburnerName;
    }

    public String getAfterburnerName() {
        return afterburnerName;
    }
}
