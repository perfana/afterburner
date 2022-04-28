package nl.stokpop.afterburner.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI afterburnerOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Afterburner API")
                        .description("SpringBoot application to test performance related metrics")
                        .version("v" + nl.stokpop.afterburner.AfterburnerTemplatedConstants.APPLICATION_VERSION)
                        .license(new License().name("MIT").url("https://opensource.org/licenses/MIT")))
                .externalDocs(new ExternalDocumentation()
                        .description("Afterburner Documentation")
                        .url("https://github.com/stokpop/afterburner"));
    }
}
