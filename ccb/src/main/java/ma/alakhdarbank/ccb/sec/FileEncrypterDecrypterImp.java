/**
 * 
 */
package ma.alakhdarbank.ccb.sec;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.Getter;
import ma.alakhdarbank.ccb.exception.RCCBAppException;

/**
 * @author a.bouabidi
 *
 */
@Getter
@Component
public class FileEncrypterDecrypterImp implements FileEncrypterDecrypter {

	
	private SecretKey  secretKey;
	
	private Cipher aes;
	
	private boolean initialized=false;
	
	
	@Autowired	
	private SyncEncrypterDecrypter syncEncrypterDecrypterImp;
	
	

	@Override
	public void init() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
		// Generate Random Symmetric AES Key 
		SecureRandom random = new SecureRandom();
		// Encrypt data with AES 
		byte[] keyData = random.generateSeed(16);
		secretKey = new SecretKeySpec(keyData, "AES"); 		
		this.aes = Cipher.getInstance("AES/CBC/PKCS5Padding");
		byte[] ivParams = new byte[aes.getBlockSize()]; 
		IvParameterSpec iv = new IvParameterSpec(ivParams); 
		this.aes.init(Cipher.ENCRYPT_MODE, secretKey, iv);
		this.initialized=true;
	}

	@Override
	public OutputStream encrypt (String json, String outfile) throws RCCBAppException {	
		if (!initialized) throw new RCCBAppException ("Not initialized");
		FileOutputStream fileOut = null;
		// json : correspond an declaration sous format JSON a chiffrer 
		//byte[] cipherrext = aes.doFinal(msg.getBytes()); 
		try {
		    	fileOut = new FileOutputStream(outfile);
		    	CipherOutputStream cipherOut = new CipherOutputStream(fileOut, aes);
		        //TODO check if iv ust be set here
		    	fileOut.write(aes.getIV());
		    	
		    	//
		        cipherOut.write(json.getBytes());
		        //		        
		    } catch (FileNotFoundException e) {
				throw new RCCBAppException(e);
			} catch (IOException e) {
				throw new RCCBAppException(e);
			}
		
		/*
		// Lecture du certificat (cle publique RSA) 
		PublicKey clePublique = GestionClesRSA.lectureClePublique(pathClepublic); 
		// Encrypt AES key (for AE5-256 it's 32 bytes) with RSA private key. 
		Cipher cipher = Cipher.getInstance("RSA"); cipher.init(Cipher.WRAP_MODE, clePublique); 
		// wrappedKey : correspond a la cle AES cryptee via RSA 
		// I1 doit etre transmis an niveau du champs "token" du Header de la requete 
		byte[] wrappedKey = cipher.wrap(skeySpec); 
		// Procider a l'encodage du token et des declarations avant transmission 
		// Le champs encodedData correspond au donnees a transmettre dans le body de la requete 
		// Le champs encodedioken Correspond a la cle a transmettre au niveau du champs "token" du Header 
		Base64 codec - new Bese64(); 
		String encodedData = codec.encodeBase64String(cipherText); 
		String encodedioken - codec.encodeBase64String(wrappedKey); 
		*/
	   
		return fileOut;
	}

	@Override
	public byte[] encrypt (String json) throws RCCBAppException {		
		if (!initialized) throw new RCCBAppException ("Not initialized");
		byte[] cipherrext = null;
		try {
			cipherrext = aes.doFinal(json.getBytes());			
		} catch (IllegalBlockSizeException | BadPaddingException e) {
			throw new RCCBAppException(e);
		} 		    	
		
		
		/*
		// Lecture du certificat (cle publique RSA) 
		PublicKey clePublique = GestionClesRSA.lectureClePublique(pathClepublic); 
		// Encrypt AES key (for AE5-256 it's 32 bytes) with RSA private key. 
		Cipher cipher = Cipher.getInstance("RSA"); cipher.init(Cipher.WRAP_MODE, clePublique); 
		// wrappedKey : correspond a la cle AES cryptee via RSA 
		// I1 doit etre transmis an niveau du champs "token" du Header de la requete 
		byte[] wrappedKey = cipher.wrap(skeySpec); 
		// Procider a l'encodage du token et des declarations avant transmission 
		// Le champs encodedData correspond au donnees a transmettre dans le body de la requete 
		// Le champs encodedioken Correspond a la cle a transmettre au niveau du champs "token" du Header 
		Base64 codec - new Bese64(); 
		String encodedData = codec.encodeBase64String(cipherText); 
		String encodedioken - codec.encodeBase64String(wrappedKey); 
		*/
	   
		return cipherrext;
	}


	

}

