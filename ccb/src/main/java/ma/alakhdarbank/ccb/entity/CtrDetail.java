/**
 * 
 */
package ma.alakhdarbank.ccb.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.Generated;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class CtrDetail {
	
	@Id
	@JsonIgnore
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@JsonProperty("e06")
	private Long idLigne;
	
	@JsonProperty("e07")
	private String codeZone;
	
	@JsonProperty("e08")
	private String codeErreur;
	
	@JsonProperty("e09")
	private String gravite;
	
	@JsonIgnore
	@Column(name = "ctr_id")
	private Long ctrId;
	
}
