package ma.akhdarbank.apps.clients;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ma.akhdarbank.apps.RestClientsFactory;
import ma.akhdarbank.apps.excp.DWJCallException;
import ma.akhdarbank.apps.model.TicketBatch;
import ma.akhdarbank.apps.model.TierBatch;
import ma.akhdarbank.apps.model.TierBatchs;
import ma.akhdarbank.apps.model.TierBatch.TierBatchRep;


@Component
public class ApiBatchMatchingClientImp implements ApiBatchMatchingClient {
	
	@Autowired
	private RestClientsFactory clientfactory;
	
	
	@Override
	public Long prepareDataForMathing(String token, TierBatchs tiers) throws DWJCallException {
		RestTemplate client = clientfactory.createApiBatchMatchingClient();
		String url = clientfactory.getPrepMathingUrl();
		HttpHeaders headers = new HttpHeaders();
		//
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    headers.setBearerAuth(token);
	    HttpEntity<TierBatchs> entity = new HttpEntity<TierBatchs>(tiers, headers);
	    //ObjectMapper objectMapper = new ObjectMapper();	    
		ResponseEntity<TicketBatch> rp  = client.postForEntity(url, entity, TicketBatch.class);		
		if (rp.getStatusCode().equals(HttpStatus.OK))		
		return rp.getBody().getTicket();	
		else {
			throw new DWJCallException (" API Return Error "+rp.getStatusCodeValue());
			
		}
		
	}

	@Override
	public TierBatchRep[] getDataAfterMathing(String token, String tiket) {
		RestTemplate client = clientfactory.createApiBatchMatchingClient();
		String url = clientfactory.getGetMathingUrl();
		HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    headers.setBearerAuth(token);	    
		String body ="{\"ticket\":"+tiket + "}";
		HttpEntity<String> entity = new HttpEntity<String>(body, headers);
        ResponseEntity< TierBatchRep[]> response = client.postForEntity(url, entity, TierBatchRep[].class);
        TierBatchRep[] tierBatchData = response.getBody();
        //
		return tierBatchData;		
	}

}
