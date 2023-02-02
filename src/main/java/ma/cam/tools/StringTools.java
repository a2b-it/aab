package ma.cam.tools;

import java.security.MessageDigest;

import javax.xml.bind.DatatypeConverter;

public class StringTools {
	// Java method to create SHA-25 checksum
		public static String getSHA256Hash(String data) {
	        String result = null;
	        try {
	            MessageDigest digest = MessageDigest.getInstance("SHA-256");
	            byte[] hash = digest.digest(data.getBytes("UTF-8"));
	            return bytesToHex(hash); // make it printable
	        }catch(Exception ex) {
	            ex.printStackTrace();
	        }
	        return result;
	    }
		
		/**
	     * Use javax.xml.bind.DatatypeConverter class in JDK to convert byte array
	     * to a hexadecimal string. Note that this generates hexadecimal in lower case.
	     * @param hash
	     * @return 
	     */
	    private static String  bytesToHex(byte[] hash) {
	        return DatatypeConverter.printHexBinary(hash).toLowerCase();
	    }
}
