package ma.akhdarbank.apps.clients;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import ma.akhdarbank.apps.RestClientsFactory;
import ma.akhdarbank.apps.model.User;



public class ApiAuthClientImp implements ApiAuthClient {

	
	@Autowired
	RestClientsFactory clientfactory;
	
	@Override
	public String auth(String user, String password) {
		RestTemplate client = clientfactory.createApiAuthClient();
		String url = clientfactory.getAuthUrl();
		ResponseEntity<User> ru = client.getForEntity(url, User.class);
		return ru.getBody().getToken();
	}

}
