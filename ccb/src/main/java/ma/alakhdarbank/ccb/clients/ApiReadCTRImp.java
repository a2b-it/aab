/**
 * 
 */
package ma.alakhdarbank.ccb.clients;

import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
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
public class ApiReadCTRImp implements ApiReadCTR {
	@Autowired
	RestClientsFactory clientfactory;
	
	
	@Override
	public String read( Map<String, String> headers) throws RCCBAppException {
		String url = clientfactory.getApiReceiveCCBCTR();
		RestTemplate client = clientfactory.createApiDataCCB();
		HttpHeaders httphead = new HttpHeaders();
		
		if (headers!= null)	{			
			//httphead.setContentType(MediaType.TEXT_PLAIN);
			httphead.setAccept(Collections.singletonList(MediaType.ALL));
		}
		for (Map.Entry<String,String> entry : headers.entrySet()) {
			httphead.add(entry.getKey(), entry.getValue());			
		}
		HttpEntity<Map<String, String>> entity = new HttpEntity(null,httphead);		
		ResponseEntity<String> re =client.exchange(url, HttpMethod.GET, entity, String.class);
		
		if (re.getStatusCodeValue()==200) {			
			return re.getBody();
		}else {
			throw new  RCCBAppException ("Erreur lors du retour statut="+re.getStatusCode());
		}
		
	}

}
