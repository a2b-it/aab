package ma.akhdarbank.apps.clients;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import ma.akhdarbank.apps.RestClientsFactory;
import ma.akhdarbank.apps.excp.DWJCallException;



@Component
public class ApiAuthClientImp implements ApiAuthClient {

	
	@Autowired
	RestClientsFactory clientfactory;
	
	@Value("${cam.auth.api.user}")
	public String apiUserLogin;
	
	
	@Value("${cam.auth.api.password}")
	public String apiUserPassword;
	
	
	@Override
	public String auth() throws DWJCallException {
		RestTemplate client = clientfactory.createApiAuthClient();
		String url = clientfactory.getAuthUrl();
		String body = new StringBuilder().append("{\"").append("username\":\"").
				append(apiUserLogin).append("\",\"password\":\"").append(apiUserPassword).append("\"}").toString();
		HttpHeaders headers = new HttpHeaders();
		//headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<>(body, headers);
		String token = null;
		try {
			
		ResponseEntity<String> ru = client.postForEntity(url,entity,String.class);
		ObjectMapper om = new ObjectMapper ();
		JsonNode root, result;
		
			root = om.readTree(ru.getBody());
			result = root.path("result");
			token = result.path("token").asText();
		} catch (JsonProcessingException e) {
			throw new DWJCallException(e); 
		}catch (Exception e) {
			throw new DWJCallException(e);
		}
		
		return token;
	}

}
