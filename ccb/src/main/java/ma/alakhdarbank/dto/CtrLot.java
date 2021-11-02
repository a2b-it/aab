/**
 * 
 */
package ma.alakhdarbank.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author a.bouabidi
 *
 */
@Getter
@Setter
public class CtrLot {
	
	 private String serviceBAM;
	 private String idLot;
	 private String emetteur;
	 private String recepteur;
	 private String dateArrete;
	 private String password_hash;
	 private String login;
}
