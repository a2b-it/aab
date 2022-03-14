package ma.ab.banking.app;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.annotation.TypeAlias;

@SpringBootApplication
public class CoreEthixApplication {

	public static void main(String[] args) {
		SpringApplication.run(CoreEthixApplication.class, args);
	}

}
