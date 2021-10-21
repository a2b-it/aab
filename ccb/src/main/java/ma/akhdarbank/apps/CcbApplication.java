package ma.akhdarbank.apps;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"ma.akhdarbank.apps","ma.alakhdarbank.ccb","ma.alakhdarbank.apps.rest"})
@EnableBatchProcessing
public class CcbApplication {

	public static void main(String[] args) {
		SpringApplication.run(CcbApplication.class, args);
		
	}

}
