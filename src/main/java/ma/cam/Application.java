package ma.cam;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@Configuration
@ComponentScan
@EnableScheduling
@EnableJms
public class Application  {
	
	private static final Logger LOGGER=LoggerFactory.getLogger(Application.class);
	 static String logData ="============================== Démarrage de l'application {"+new Date()+"} ===========================";
	public static void main(String[] args) {
		LOGGER.trace(logData);
		LOGGER.debug(logData);
		LOGGER.info(logData);          
		LOGGER.warn(logData);          
		LOGGER.error(logData);
		SpringApplication.run(Application.class, args);
	}

	

}
