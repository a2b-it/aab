package ma.akhdarbank.apps.clients;


import org.springframework.beans.factory.annotation.Autowired;
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
	    //ObjectMapper objectMapper = new ObjectMapper();	    
		ResponseEntity<TicketBatch> rp  = client.postForEntity(url, tiers, TicketBatch.class);		
		if (rp.getStatusCode().equals(HttpStatus.OK))		
		return rp.getBody().getTicket();	
		else {
			throw new DWJCallException (" API Return Error "+rp.getStatusCodeValue());
			
		}
		
	}

	@Override
	public TierBatch[] getDataAfterMathing(String token, Long tiket) throws JsonProcessingException {
		RestTemplate client = clientfactory.createApiBatchMatchingClient();
		String url = clientfactory.getGetMathingUrl();
		HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    headers.setBearerAuth(token);
	    ObjectMapper objectMapper = new ObjectMapper();	    
	    String body = objectMapper.writeValueAsString(tiket);
		ResponseEntity<TierBatch[]> rp  = client.postForEntity(url, body, TierBatch[].class);		
		return rp.getBody();		
	}

}
