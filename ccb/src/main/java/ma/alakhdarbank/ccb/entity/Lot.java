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
	
	private String nomfichier ;
	
	private STATUT status;
	
	private Date dateenvoi;
	
	private Integer idCtr;
	
	
	public enum STATUT {
		ENVOYER,
		ACCEPTER,
		REJETER
	}
	

}
