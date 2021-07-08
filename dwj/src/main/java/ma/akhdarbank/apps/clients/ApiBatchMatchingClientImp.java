package ma.akhdarbank.apps.clients;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ma.akhdarbank.apps.RestClientsFactory;
import ma.akhdarbank.apps.model.TicketBatch;
import ma.akhdarbank.apps.model.TierBatch;
import ma.akhdarbank.apps.model.TierDTO;

public class ApiBatchMatchingClientImp implements ApiBatchMatchingClient {

	
	@Autowired
	RestClientsFactory clientfactory;
	
	
	@Override
	public Long prepareDataForMathing(String token, List<TierDTO> tiers) {
		RestTemplate client = clientfactory.createApiBatchMatchingClient();
		String url = clientfactory.getPrepMathingUrl();
		HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    headers.setBearerAuth(token);
	    //ObjectMapper objectMapper = new ObjectMapper();	    
		ResponseEntity<TicketBatch> rp  = client.postForEntity(url, tiers, TicketBatch.class);		
		return rp.getBody().getTicket();		
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
