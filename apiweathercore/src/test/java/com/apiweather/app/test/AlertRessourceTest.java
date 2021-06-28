package com.apiweather.app.test;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.annotation.Order;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.Base64Utils;

import com.apiweather.app.biz.model.Alert;
import com.apiweather.app.biz.model.AlertStatus;
import com.apiweather.app.biz.repo.AlertRepository;
import com.apiweather.app.rest.AlertRessource;
import com.fasterxml.jackson.databind.ObjectMapper;

@TestPropertySource(locations = "classpath:application-test.properties")
@SpringBootTest(classes = AlertRessource.class)
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
public class AlertRessourceTest {
	@Autowired
	MockMvc mockMvc;

	private List<Alert> alerts = new ArrayList<Alert>();

	@MockBean
	AlertRepository alertRepository;

	
	public void testCheckAlert() throws Exception {
		mockMvc.perform(get("/alert/check").header("Authorization",
				" Basic " + Base64Utils.encodeToString("user:password".getBytes())))
				// .header("Authorization", "Bearer " + accessToken))
				.andExpect(status().isOk()).andExpect(jsonPath("$", is("OK Alert")));

		// .andExpect(jsonPath("$[0].firstName", is("Laurent")));
	}

	@Test
	public void testAddAlert() throws Exception {
		Alert alert = new Alert();
		alert.setDate(new Date());
		alert.setDetails("Deatil de l'alerte ....... ..... Fin");
		alert.setLevel(2);
		alert.setStatus(AlertStatus.draft);
		alert.setSource(3);

		ObjectMapper mapper = new ObjectMapper();
		String s = mapper.writeValueAsString(alert);
		MvcResult r = mockMvc
				.perform(post("/alert/add").content(s).header("Authorization",
						" Basic " + Base64Utils.encodeToString("user:password".getBytes())))
				.andExpect(status().isOk()).andReturn();
		Alert rv = mapper.readValue(r.getResponse().getContentAsString(), Alert.class);
		alerts.add(rv);
		// .andExpect(jsonPath("$[0].firstName", is("Laurent")));
	}

	
	public void deleteById() throws Exception {
		Alert a = alerts.get(0);
		mockMvc.perform(get("/alert/remove/" + a.getIdAlert()).header("Authorization",
				" Basic " + Base64Utils.encodeToString("user:password".getBytes())))
				// .header("Authorization", "Bearer " + accessToken))
				.andExpect(status().isOk()).andExpect(jsonPath("$", is("OK Alert")));

		// .andExpect(jsonPath("$[0].firstName", is("Laurent")));
	}

	public void updateById() throws Exception {
		mockMvc.perform(get("/alert/check").header("Authorization",
				" Basic " + Base64Utils.encodeToString("user:password".getBytes())))
				// .header("Authorization", "Bearer " + accessToken))
				.andExpect(status().isOk()).andExpect(jsonPath("$", is("OK Alert")));

		// .andExpect(jsonPath("$[0].firstName", is("Laurent")));
	}
}
