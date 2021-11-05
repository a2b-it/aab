/**
 * 
 */
package ma.alakhdarbank.ccb.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

/**
 * @author a.bouabidi
 *
 */
@Getter
@Setter
@Entity(name = "Ctr_Detail")
public class CtrDeatil {
	
	@Id
	@JsonProperty("e06")
	private Long idLigne;
	
	@JsonProperty("e07")
	private String codeZone;
	
	@JsonProperty("e08")
	private String codeErreur;
	
	@JsonProperty("e09")
	private String gravite;
	
	
}
