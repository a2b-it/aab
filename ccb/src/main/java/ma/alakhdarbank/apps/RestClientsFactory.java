package ma.alakhdarbank.apps;

import java.time.Duration;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import lombok.Getter;


@Component
public class RestClientsFactory {
	
	RestTemplateBuilder builder ;
	
	@Getter
	@Value("${bkam.receiveCCBCTR.url}")
	public String apiReceiveCCBCTR;
	
	@Getter
	@Value("${bkam.auth.url}")
	private String authUrl;
	
	@Getter
	@Value("${bkam.sendCCBData.url}")
	private String sendingApiUrl;
	
	
	
	@Autowired
	public RestClientsFactory(RestTemplateBuilder builder) {
		super();
		this.builder = builder;
	}


	@Autowired
	public RestTemplate createApiAuthClient()  {
		
		return createDefault() ;	
	
	}

	
	@Autowired
	public RestTemplate createApiDataCCB()  {
		
		return createDefault() ;
	
	}
	
	@Autowired
	public RestTemplate createApiDataCCB_CTR()  {
		
		return createDefault() ;
	}

	private RestTemplate createDefault()  {
		RestTemplate client = builder				
				.setConnectTimeout(Duration.ofMillis(3000))
	            .setReadTimeout(Duration.ofMillis(3000)).build();
		client.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
		return client;
	}
	
	
}
