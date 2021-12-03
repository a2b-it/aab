/**
 * 
 */
package ma.alakhdarbank.ccb.clients;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javassist.bytecode.Descriptor.Iterator;
import ma.alakhdarbank.apps.RestClientsFactory;

/**
 * @author a.bouabidi
 *
 */
@Component
public class ApiReadCTRImp implements ApiReadCTR {
	@Autowired
	RestClientsFactory clientfactory;
	
	
	@Override
	public String read( Map<String, String> headers) {
		String url = clientfactory.getApiReceiveCCBCTR();
		RestTemplate client = clientfactory.createApiDataCCB();
		
		ResponseEntity<String> re = client.getForEntity(url, String.class, headers);
		if (re.getStatusCodeValue()==200) {
			
			return re.getBody();
		}
		return null;
	}

}
