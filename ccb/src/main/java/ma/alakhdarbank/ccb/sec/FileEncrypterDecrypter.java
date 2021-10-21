/**
 * 
 */
package ma.alakhdarbank.ccb.sec;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.NoSuchPaddingException;

import ma.alakhdarbank.ccb.exception.RCCBAppException;

/**
 * @author a.bouabidi
 *
 */
public interface FileEncrypterDecrypter {
	
	
	public OutputStream encrypt (String content, String fileOut) throws RCCBAppException;
	
	public Key getSecretKey ();
	
	public void init () throws  NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException;

}
