package com.apiweather.app.test;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import com.apiweather.app.ApiweatherCoreApplication;




class ApiweatherCoreApplicationTests {
	
 /*
  * @Autowired
    MockMvc mockMvc;
	
	 @Test
	    public void testCheckAlert() throws Exception {
	        mockMvc
	        	.perform(get("/alert/check")
	        			.header("Authorization", " Basic " + Base64Utils.encodeToString("user:password".getBytes()) ))
	        			//.header("Authorization", "Bearer " + accessToken))
	            .andExpect(status().isOk());
	            //.andExpect(jsonPath("$[0].firstName", is("Laurent")));
	    }
  */
}
