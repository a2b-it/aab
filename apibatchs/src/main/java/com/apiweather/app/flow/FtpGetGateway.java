/**
 * 
 */
package com.apiweather.app.flow;

import java.io.File;
import java.util.List;

import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.stereotype.Component;

/**
 * @author a.bouabidi
 *
 */


public interface FtpGetGateway {
	
	
	File retreive(String directory);
	

}
