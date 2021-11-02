package ma.alakhdarbank.apps;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"ma.alakhdarbank.apps","ma.alakhdarbank.ccb"})
@EnableJpaRepositories("ma.alakhdarbank.ccb.persistence")
@EntityScan("ma.alakhdarbank.ccb.entity")
@EnableBatchProcessing
public class CcbApplication {

	public static void main(String[] args) {
		SpringApplication.run(CcbApplication.class, args);
		
	}

}
