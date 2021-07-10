package ma.akhdarbank.apps.clients;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

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
		ResponseEntity<User> ru = client.postForEntity(url,body, User.class);
		return ru.getBody().getToken();
	}

}
