/**
 * 
 */
package ma.alakhdarbank.ccb.entity;

import java.util.Date;

import javax.persistence.Column;
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
	
	@Column(columnDefinition="number(10,0)")
	private Long idLot;
	
	@Column(columnDefinition="number(10,0)")
	private Integer nbrCpt;
	
	private String nomfichier ;
	
	@Column(columnDefinition="number(10,0)")
	private int status;
	
	private Date dateEnvoi;
	
	@Column(columnDefinition="number(10,0)")
	private Long idCtr;
	
	private Date dateArrete;
	
	
	public enum STATUT {
		SENDING(1),
		ENVOYER(2),
		ENCOURS(3),
		ACCEPTER(4),
		REJETER(5);
		
		private final int value;
	    private STATUT(int value) {
	        this.value = value;
	    }

	    public int getValue() {
	        return value;
	    }
	}
	

}
