package io.perfana.afterburner;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

// exclude to avoid log WARN "with default user credentials"
@SpringBootApplication(exclude = { UserDetailsServiceAutoConfiguration.class })
@EnableAsync
@EnableScheduling
@ConfigurationPropertiesScan
@OpenAPIDefinition
// make specific mapper scan to avoid log: "WARN No MyBatis mapper was found in '[io.perfana.afterburner]' package."
@MapperScan(basePackages = "io.perfana.afterburner.mybatis")
public class Afterburner {

    public static void main(String[] args) {
		SpringApplication.run(Afterburner.class, args);
	}

}
