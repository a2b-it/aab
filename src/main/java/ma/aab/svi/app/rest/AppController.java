/**
 * 
 */
package ma.aab.svi.app.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ma.aab.svi.app.config.P2PwithCerts;

/**
 * @author a.bouabidi
 *
 */

@RestController
@RequestMapping(value = "/")
public class AppController {
	
	@Autowired
	P2PwithCerts p2p;
	
	
	@GetMapping("/testConnection")
	public void textConnection () {
		try {
			p2p.testconnection();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
