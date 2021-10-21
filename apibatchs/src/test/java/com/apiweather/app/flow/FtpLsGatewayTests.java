/**
 * 
 */
package com.apiweather.app.flow;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author a.bouabidi
 *
 */
@SpringBootTest
public class FtpLsGatewayTests {
	
	@Autowired
	FtpLsGateway toFtpFlowGateway;
	
	
	@Test
	public void getFile (){
		//toFtpFlowGateway.lsGetAndRmFiles("/");
		
	}

}
