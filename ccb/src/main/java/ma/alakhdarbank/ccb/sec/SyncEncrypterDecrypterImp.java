/**
 * 
 */
package ma.alakhdarbank.ccb.sec;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ma.alakhdarbank.ccb.exception.RCCBAppException;

/**
 * @author a.bouabidi
 *
 */
@Component
public class SyncEncrypterDecrypterImp implements SyncEncrypterDecrypter {
	
	@Autowired
	RSAKeyManager rsaKeyManagerImp;
	
	private Base64.Encoder codec = Base64.getEncoder(); 
	
	private Cipher cipher;

	@Override
	public byte[] encrypt(Key key) throws InvalidKeyException, IllegalBlockSizeException {
		
		return cipher.wrap(key);
	}

	@Override
	public void init(String pubCertPath) throws RCCBAppException  {		
		 try {
			// Lecture du certificat (cle publique RSA) 
			 PublicKey publicKey =rsaKeyManagerImp.getPublicKeyFromRessource(pubCertPath, "RSA");
			// Encrypt AES key (for AE5-256 it's 32 bytes) with RSA private key. 
			this.cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.WRAP_MODE, publicKey);
			
		} catch (InvalidKeyException e) {
			throw new RCCBAppException(e);
		} catch (NoSuchPaddingException e) {
			throw new RCCBAppException(e);
		} catch (NoSuchAlgorithmException e) {
			throw new RCCBAppException(e);
		} 

	}

	@Override
	public String encodeData(byte[] data) {		
		return codec.encodeToString(data);
	}

}
