package nl.stokpop.afterburner.config;

import com.fasterxml.classmate.TypeResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.AlternateTypeRules;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static springfox.documentation.schema.AlternateTypeRules.DIRECT_SUBSTITUTION_RULE_ORDER;

@Configuration
@EnableSwagger2
public class Swagger2Config {

    private final TypeResolver typeResolver;

    public Swagger2Config(TypeResolver typeResolver) {
        this.typeResolver = typeResolver;
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
            .alternateTypeRules(
                AlternateTypeRules.newRule(
                    typeResolver.resolve(ResponseEntity.class, typeResolver.resolve(Resource.class)),
                    typeResolver.resolve(MultipartFile.class), DIRECT_SUBSTITUTION_RULE_ORDER))
            .useDefaultResponseMessages(false)
            .select()
            .apis(RequestHandlerSelectors.basePackage("nl.stokpop.afterburner"))
            .paths(PathSelectors.any())
            .build()
            .apiInfo(apiEndPointsInfo());
    }

    private ApiInfo apiEndPointsInfo() {
        return new ApiInfoBuilder().title("Spring Boot REST API")
            .description("Afterburner REST API")
            .contact(new Contact("Peter Paul Bakker", "www.stokpop.nl", "peter.paul.bakker[at]stokpop.nl"))
            .license("Apache 2.0")
            .licenseUrl("http://www.apache.org/licenses/LICENSE-2.0.html")
            .version("1.0.0")
            .build();
    }

}
