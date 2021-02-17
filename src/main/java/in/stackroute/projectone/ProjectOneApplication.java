package in.stackroute.projectone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * This is the main file that loads up the Spring Boot Application.
 * 
 * @author Ajinkya
 *
 */
@SpringBootApplication
@EnableSwagger2
public class ProjectOneApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjectOneApplication.class, args);
	}

	@Bean
	public Docket cplayerApi() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage("in.stackroute.projectone")).build();
	}

}
