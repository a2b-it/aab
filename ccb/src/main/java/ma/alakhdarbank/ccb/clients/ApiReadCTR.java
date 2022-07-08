/**
 * 
 */
package ma.alakhdarbank.ccb.clients;

import java.util.Map;

import ma.alakhdarbank.ccb.entity.Ctr;
import ma.alakhdarbank.ccb.exception.RCCBAppException;

/**
 * @author a.bouabidi
 *
 */
public interface ApiReadCTR {
	
	public String read( Map<String, String> headers) throws RCCBAppException;
}
