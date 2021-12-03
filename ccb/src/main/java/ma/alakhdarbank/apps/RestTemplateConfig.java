/**
 * 
 */
package ma.alakhdarbank.apps;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;

import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties.Security.TrustStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * @author a.bouabidi
 *
 */
public class RestTemplateConfig {
	@Bean
	public RestTemplate restTemplate() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
    		TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;
 
    		SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom()
                    		.loadTrustMaterial(null, acceptingTrustStrategy)
                    		.build();
 
    		SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);
 
    		CloseableHttpClient httpClient = HttpClients.custom()
                    		.setSSLSocketFactory(csf)
                    		.build();
 
    		HttpComponentsClientHttpRequestFactory requestFactory =
                    		new HttpComponentsClientHttpRequestFactory();
 
    		requestFactory.setHttpClient(httpClient);
    		RestTemplate restTemplate = new RestTemplate(requestFactory);
   		return restTemplate;
 	}

}
