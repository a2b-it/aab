/**
 * 
 */
package ma.alakhdarbank.ccb.clients;

import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import ma.alakhdarbank.apps.RestClientsFactory;
import ma.alakhdarbank.ccb.exception.RCCBAppException;

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
	public void send(String data, Map<String, String> headers) throws RCCBAppException {
	
		String url = clientfactory.getSendingApiUrl();
		RestTemplate client = clientfactory.createApiDataCCB();
		HttpHeaders httphead = null;
		
		if (headers!= null)	{
			httphead = new HttpHeaders();
			httphead.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			httphead.setAccept(Collections.singletonList(MediaType.APPLICATION_OCTET_STREAM));
		}
		for (Map.Entry<String,String> entry : headers.entrySet()) {
			httphead.add(entry.getKey(), entry.getValue());			
		}
		HttpEntity<Map<String, Object>> entity = new HttpEntity(data, httphead);
		
		ResponseEntity<String> re = client.postForEntity(url, entity, String.class);
		if (re.getStatusCodeValue() != 200) {
			//TODO throw exception
			throw new RCCBAppException(re.getBody().toString());
		}
		

	}

}
