/**
 * 
 */
package ma.alakhdarbank.ccb.sec;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import org.springframework.stereotype.Component;

import ma.alakhdarbank.ccb.exception.RCCBAppException;

/**
 * @author a.bouabidi
 *
 */
@Component
public class RSAKeyManagerImp implements RSAKeyManager {
	
	private PublicKey publicKey;
	
	
	@Override
	public PublicKey getPublicKeyFromFile(String path, String algorithm) throws  RCCBAppException{
		byte[] keyBytes = null;
		try {
			keyBytes = Files.readAllBytes(Paths.get(path));
			X509EncodedKeySpec  spec =
				      new X509EncodedKeySpec (keyBytes);
			KeyFactory kf;
			kf = KeyFactory.getInstance(algorithm);//"RSA"
			publicKey=kf.generatePublic(spec);	
			return publicKey;	
			
		} catch (IOException e1) {
			throw new RCCBAppException(e1);
		} catch (NoSuchAlgorithmException e) {
			throw new RCCBAppException(e);
		} catch (InvalidKeySpecException e) {
			throw new RCCBAppException(e);
		} 	
		
	
	}
	
	@Override
	public PublicKey getPublicKeyFromRessource(String name, String algorithm) throws RCCBAppException 
	{
		
		URL url = getClass().getClassLoader().getResource(name);
		byte[] keyBytes;
		
			try {
				keyBytes = Files.readAllBytes(Paths.get(url.toURI()));
				X509EncodedKeySpec  spec =
					      new X509EncodedKeySpec (keyBytes);
				KeyFactory kf;	
				kf = KeyFactory.getInstance(algorithm);//"RSA"
				publicKey=kf.generatePublic(spec);	
				return publicKey;	
			} catch (IOException e) {
				throw new RCCBAppException(e);
			} catch (URISyntaxException e) {
				throw new RCCBAppException(e);
			} catch (NoSuchAlgorithmException e) {
				throw new RCCBAppException(e);
			} catch (InvalidKeySpecException e) {
				throw new RCCBAppException(e);
			}		
		
				
	}

}
