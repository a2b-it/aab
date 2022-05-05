package ma.ab.banking.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jms.annotation.EnableJms;

@SpringBootApplication
@EnableJms
@ComponentScan(basePackages = "ma.ab.banking.app")
public class CoreEthixApplication {

	public static void main(String[] args) {
		SpringApplication.run(CoreEthixApplication.class, args);
	}

}
