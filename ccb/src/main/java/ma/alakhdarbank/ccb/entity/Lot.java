/**
 * 
 */
package ma.alakhdarbank.ccb.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * @author a.bouabidi
 *
 */
@Entity
@Table(name = "LOT")
@Getter
@Setter
public class Lot {
	@Id
    @GeneratedValue
    private Long id;
	
	private Integer idLot;
	
	private Integer nbrCpt;
	
	private String nomfichier ;
	
	private STATUT status;
	
	private Date dateEnvoi;
	
	private Integer idCtr;
	
	private Date dateArrete;
	
	
	public enum STATUT {
		SENDING,
		ENVOYER,
		ACCEPTER,
		REJETER
	}
	

}
