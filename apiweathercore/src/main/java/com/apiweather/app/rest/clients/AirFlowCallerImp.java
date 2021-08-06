package com.apiweather.app.rest.clients;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.apiweather.app.inner.model.airflow.Dag;
import com.apiweather.app.inner.model.airflow.ListDag;

@Component
public class AirFlowCallerImp implements AirFlowCaller {
	
	@Autowired
	RestClientsFactory clientfactory;

	@Override
	public ListDag ListDAGs() {
		String url = "/dags";
		
		HashMap<String, Object> params = new HashMap();
		params.put("limit", 100);
		params.put("offset", 0);
		//params.put("order_by", 100);
		params.put("only_active", true);
						
		RestTemplate client = clientfactory.createAirFlowClient();
		
		UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl (client.getUriTemplateHandler().toString()+url)
                .queryParam("limit", 100)
                .queryParam("offset",0)
                //.queryParam("order_by",0)
                .queryParam("only_active",true);
		ResponseEntity r = client.postForEntity(uriBuilder.build(params), null, ListDag.class);
		if (r.getStatusCodeValue() == 200) {
			return ((ListDag)r.getBody());
		}
		
		/*
		 String url = "http://localhost:" + port + "/users/" + userId;

        //setting up the HTTP Basic Authentication header value
        String authorizationHeader = "Basic " + DatatypeConverter.printBase64Binary((username + ":" + password).getBytes());

        HttpHeaders requestHeaders = new HttpHeaders();
        //set up HTTP Basic Authentication Header
        requestHeaders.add("Authorization", authorizationHeader);
        requestHeaders.add("Accept", MediaType.APPLICATION_JSON_VALUE);

        //request entity is created with request headers
        HttpEntity<AddUserRequest> requestEntity = new HttpEntity<>(requestHeaders);

        //adding the query params to the URL
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("name", "chathuranga")
                .queryParam("email", "chathuranga.t@gmail.com");

        ResponseEntity<FindUserResponse> responseEntity = restTemplate.exchange(
                uriBuilder.toUriString(),
                HttpMethod.GET,
                requestEntity,
                FindUserResponse.class
        );

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            System.out.println("response received");
            System.out.println(responseEntity.getBody());
        } else {
            System.out.println("error occurred");
            System.out.println(responseEntity.getStatusCode());
        }*/
		return null;
	}

	@Override
	public Dag GetBasicInformation() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Dag getJobStatus() {
		// TODO Auto-generated method stub
		return null;
	}

}
