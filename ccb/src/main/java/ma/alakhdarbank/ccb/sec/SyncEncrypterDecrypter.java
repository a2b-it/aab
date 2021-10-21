/**
 * 
 */
package ma.alakhdarbank.ccb.sec;

import java.security.InvalidKeyException;
import java.security.Key;

import javax.crypto.IllegalBlockSizeException;

import ma.alakhdarbank.ccb.exception.RCCBAppException;

/**
 * @author a.bouabidi
 *
 */
public interface SyncEncrypterDecrypter {
	
	public byte[] encrypt(Key key) throws InvalidKeyException, IllegalBlockSizeException;
	
	
	public void init (String pubCertPath) throws RCCBAppException;
	
	
	public String encodeData (byte[] data);

}
