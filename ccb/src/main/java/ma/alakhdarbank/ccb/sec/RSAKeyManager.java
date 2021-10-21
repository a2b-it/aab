/**
 * 
 */
package ma.alakhdarbank.ccb.sec;

import java.security.PublicKey;

import ma.alakhdarbank.ccb.exception.RCCBAppException;

/**
 * @author a.bouabidi
 *
 */
public interface RSAKeyManager {
	
	
	public PublicKey getPublicKeyFromFile(String path, String algorithm) throws RCCBAppException;
	public PublicKey getPublicKeyFromRessource(String name, String algorithm) throws RCCBAppException;
	

}
