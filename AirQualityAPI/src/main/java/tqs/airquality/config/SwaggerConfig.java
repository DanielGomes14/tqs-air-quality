package tqs.airquality.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket apiDocket() {

        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("tqs.airquality"))
                .paths(PathSelectors.regex("/.*"))
                .build();

    }
    ApiInfo apiInfo() {
        return new ApiInfo(
                "TQS Air Quality REST API",
                "REST API For TQS Air Quality",
                "API TOS",
                "Terms of service",
                new Contact("Daniel Gomes", "www.ua.pt", "dagomes@ua.pt"),
                "License of API", "API license URL", Collections.emptyList());
    }
}