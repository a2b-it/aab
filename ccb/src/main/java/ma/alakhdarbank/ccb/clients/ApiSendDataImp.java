/**
 * 
 */
package ma.alakhdarbank.ccb.clients;

import java.net.URI;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import ma.alakhdarbank.apps.RestClientsFactory;

/**
 * @author a.bouabidi
 *
 */
@Component
public class ApiSendDataImp implements ApiSendData {
	@Autowired
	RestClientsFactory clientfactory;
	
	@Value("${bkam.sendCCBData.url}")
	public String apiUserLogin;
	
	
	@Override
	public void send(String data, Map<String, String> headers) {
		String url = clientfactory.getSendingApiUrl();
		RestTemplate client = clientfactory.createApiDataCCB();
		
		URI uri = UriComponentsBuilder.fromHttpUrl(url).
				queryParam("serviceBAM", "").
				queryParam("idLot", "").
				queryParam("emetteur", "").
				queryParam("recepteur", "").
				queryParam("dateDeclaration", "").
				queryParam("nbrEnregistrement", "").
				queryParam("contentType", "").
				queryParam("login", "").
				queryParam("password_hash", "").
				queryParam("token", "")
				.build().toUri();
		ResponseEntity<String> re = client.postForEntity(uri, data, String.class);
		if (re.getStatusCodeValue() != 200) {
			//TODO throw exception
		}
		

	}

}
