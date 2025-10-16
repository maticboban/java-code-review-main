package schwarz.jobs.interview.coupon.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import schwarz.jobs.interview.coupon.constants.SpringProfile;

/*import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;*/

@Profile(SpringProfile.NOT_PROD)
@Configuration
public class SwaggerConfiguration {

    @Value("${app.serviceUrl}")
    private String serviceUrl;

    @Bean
    public OpenAPI couponOpenAPI() {
        Server s = new Server();
        s.setUrl(serviceUrl); // set proper host
        return new OpenAPI()
            .addServersItem(s)
            .info(new Info()
                .title("Coupon API")
                .description("Coupon Management System with WebFlux")
                .version("v1.0.0"));
    }

    /*@Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
            .select()
            .apis(RequestHandlerSelectors.basePackage("schwarz.jobs.interview.coupon.controller"))
            .paths(PathSelectors.any())
            .build();
    }*/
}

