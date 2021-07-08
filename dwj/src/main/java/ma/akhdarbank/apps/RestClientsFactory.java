package ma.akhdarbank.apps;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import lombok.Getter;


@Component
public class RestClientsFactory {
	
	RestTemplateBuilder builder ;
	
	@Getter
	@Value("${cam.auth.url}")
	private String authUrl;
	
	@Getter
	@Value("${cam.prpareBatchMatching.url}")
	private String prepMathingUrl;
	
	@Getter
	@Value("${cam.getBatchMatching.url}")
	private String getMathingUrl;
	
	@Autowired
	public RestClientsFactory(RestTemplateBuilder builder) {
		super();
		this.builder = builder;
	}


	@Autowired
	public RestTemplate createApiAuthClient()  {
	
		return builder				
				.setConnectTimeout(Duration.ofMillis(3000))
	            .setReadTimeout(Duration.ofMillis(3000)).build();	
	
	}

	
	@Autowired
	public RestTemplate createApiBatchMatchingClient()  {
	
		return builder				
				.setConnectTimeout(Duration.ofMillis(3000))
	            .setReadTimeout(Duration.ofMillis(3000)).build();	
	
	}

	
	
	
}
