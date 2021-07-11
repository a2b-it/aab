package ma.akhdarbank.apps.clients;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import ma.akhdarbank.apps.RestClientsFactory;
import ma.akhdarbank.apps.model.User;



@Component
public class ApiAuthClientImp implements ApiAuthClient {

	
	@Autowired
	RestClientsFactory clientfactory;
	
	@Value("${cam.auth.api.user}")
	public String apiUserLogin;
	
	
	@Value("${cam.auth.api.password}")
	public String apiUserPassword;
	
	
	@Override
	public String auth() {
		RestTemplate client = clientfactory.createApiAuthClient();
		String url = clientfactory.getAuthUrl();
		String body = new StringBuilder().append("{\"").append("username\":\"").
				append(apiUserLogin).append(",\"password\":\"").append(apiUserPassword).append("\"}").toString();
		ResponseEntity<String> ru = client.postForEntity(url,body,String.class);
		ObjectMapper om = new ObjectMapper ();
		JsonNode root, result;
		String token = null;
		try {
			root = om.readTree(ru.getBody());
			result = root.path("result");
			token = result.path("token").asText();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return token;
	}

}
