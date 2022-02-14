/**
 * 
 */
package ma.alakhdarbank.ccb.clients;

import java.util.Map;

import ma.alakhdarbank.ccb.exception.RCCBAppException;

/**
 * @author a.bouabidi
 *
 */
public interface ApiSendData {
	
	public void send(String data, Map<String, String> headers) throws RCCBAppException;

}
