/**
 * 
 */
package com.apiweather.app.flow;

import java.util.List;

import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.stereotype.Component;

/**
 * @author a.bouabidi
 *
 */


public interface FtpLsGateway {
	
	
	List list(String directory);
	

}
