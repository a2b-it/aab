/**
 * 
 */
package ma.alakhdarbank.ccb.clients;

import java.util.Map;

/**
 * @author a.bouabidi
 *
 */
public interface ApiSendData {
	
	public void send(String data, Map<String, String> headers);

}
