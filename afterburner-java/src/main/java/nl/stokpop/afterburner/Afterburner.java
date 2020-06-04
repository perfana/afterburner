package nl.stokpop.afterburner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAsync
@EnableScheduling
@EnableConfigurationProperties(AfterburnerProperties.class)
public class Afterburner {

    public static void main(String[] args) {
		SpringApplication.run(Afterburner.class, args);
	}

}
