package ma.akhdarbank.apps;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.time.Duration;

import javax.net.ssl.SSLContext;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.TrustStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;


@Component
@Slf4j
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
	
		return restNoSslCheckTemplate ();	
	
	}

	
	@Autowired
	public RestTemplate createApiBatchMatchingClient()  {
	
		return restNoSslCheckTemplate ();	
	
	}

	private RestTemplate createDefault()  {
		RestTemplate client = builder				
				.setConnectTimeout(Duration.ofMillis(3000))
	            .setReadTimeout(Duration.ofMillis(3000)).build();
		client.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
		return client;
	}
	
	
	
	public RestTemplate restNoSslCheckTemplate() {
    		TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;
    		RestTemplate restTemplate = null;
    		SSLContext sslContext;
			try {
				sslContext = org.apache.http.ssl.SSLContexts.custom()
				        		.loadTrustMaterial(null, acceptingTrustStrategy)
				        		.build();
				SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);
				 
	    		CloseableHttpClient httpClient = HttpClients.custom()
	                    		.setSSLSocketFactory(csf)
	                    		.build();
	 
	    		HttpComponentsClientHttpRequestFactory requestFactory =
	                    		new HttpComponentsClientHttpRequestFactory();
	 
	    		requestFactory.setHttpClient(httpClient);
	    		restTemplate = new RestTemplate(requestFactory);
			} catch (KeyManagementException | NoSuchAlgorithmException | KeyStoreException e) {
				// TODO Auto-generated catch block				
				log.error(e.getMessage());
			}
 
    		
   		return restTemplate;
 	}
	
}
