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


public interface FtpSendGateway {
	
	
	String send(String directory);

}
