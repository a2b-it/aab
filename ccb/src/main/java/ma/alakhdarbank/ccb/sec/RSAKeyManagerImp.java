/**
 * 
 */
package ma.alakhdarbank.ccb.sec;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

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
	public PublicKey getPublicKeyFromFile(String path, String algorithm) throws RCCBAppException {
		InputStream in = null;
		ObjectInputStream oin = null;
		KeyFactory kf = null;
		try {

			in = new FileInputStream(path);
			oin = new ObjectInputStream(new BufferedInputStream(in));
			BigInteger m = (BigInteger) oin.readObject();
			BigInteger e = (BigInteger) oin.readObject();

			RSAPublicKeySpec spec = new RSAPublicKeySpec(m, e);

			kf = KeyFactory.getInstance(algorithm);// "RSA"
			publicKey = kf.generatePublic(spec);
			return publicKey;

		} catch (IOException e1) {
			throw new RCCBAppException(e1);
		} catch (NoSuchAlgorithmException e) {
			throw new RCCBAppException(e);
		} catch (InvalidKeySpecException e) {
			throw new RCCBAppException(e);
		} catch (ClassNotFoundException e) {
			throw new RCCBAppException(e);
		} finally {
			try {
				if (oin != null)
					oin.close();
				if (in != null)
					in.close();
			} catch (IOException e) {
				throw new RCCBAppException(e);
			}

		}

	}

	@Override
	public PublicKey getPublicKeyFromRessource(String name, String algorithm) throws RCCBAppException {

		ObjectInputStream in = null;
		URL url = getClass().getClassLoader().getResource(name);
		KeyFactory kf;
		try {
			// X509EncodedKeySpec
			// keyBytes = Files.readAllBytes(Paths.get(url.toURI()));
			in = new ObjectInputStream(Files.newInputStream(Paths.get(url.toURI())));
			RSAPublicKeySpec spec = new RSAPublicKeySpec((BigInteger) in.readObject(), (BigInteger) in.readObject());

			kf = KeyFactory.getInstance(algorithm);// "RSA"
			publicKey = kf.generatePublic(spec);
			return publicKey;
		} catch (IOException e) {
			throw new RCCBAppException(e);
		} catch (URISyntaxException e) {
			throw new RCCBAppException(e);
		} catch (NoSuchAlgorithmException e) {
			throw new RCCBAppException(e);
		} catch (InvalidKeySpecException e) {
			throw new RCCBAppException(e);
		} catch (ClassNotFoundException e) {
			throw new RCCBAppException(e);
		} finally {
			try {
				if (in != null)
					in.close();
			} catch (IOException e) {
				throw new RCCBAppException(e);
			}

		}

	}

}
