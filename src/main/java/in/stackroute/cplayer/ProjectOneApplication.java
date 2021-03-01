package in.stackroute.cplayer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import in.stackroute.cplayer.property.FileStorageProperties;

/**
 * This is the main file that loads up the Spring Boot Application.
 * 
 * @author Ajinkya
 *
 */
@SpringBootApplication
@EnableConfigurationProperties({ FileStorageProperties.class })
public class ProjectOneApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjectOneApplication.class, args);
	}

}
