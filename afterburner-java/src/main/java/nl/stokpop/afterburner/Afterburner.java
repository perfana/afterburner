package nl.stokpop.afterburner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class Afterburner {

    public static void main(String[] args) {
		SpringApplication.run(Afterburner.class, args);
	}

}
