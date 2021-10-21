/**
 * 
 */
package ma.akhdarbank.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author a.bouabidi
 *
 */
@Getter
@Setter
@NoArgsConstructor
public class Lot {
	
	String serviceBAM;
	String idLot;
	String emetteur;
	String recepteur;
	String dateDeclaration;
	String nbrEnregistrement;
	String contentType;
	String login;
	String password_hash;
	String token;

}
