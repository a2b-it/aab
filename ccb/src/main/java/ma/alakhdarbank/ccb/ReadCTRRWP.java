/**
 * 
 */
package ma.alakhdarbank.ccb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import ma.alakhdarbank.ccb.clients.ApiReadCTR;

/**
 * @author a.bouabidi
 *
 */

public class ReadCTRRWP {
	@Autowired
	private ApiReadCTR apiReadCTRImp;
	
	@Value("${bkam.receiveCCBCTR.url}")
	public String urlPath;
	

}
