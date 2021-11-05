/**
 * 
 */
package ma.alakhdarbank.ccb.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonFormat;
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
@Entity(name = "Ctr_LOT")
public class Ctr {
	
	@Id
	@JsonIgnore
	private Long id;
	
	@JsonProperty("e01")
	private int statut;
	
	@JsonProperty("e02")
	private String code_dec;
	
	@JsonProperty("e03")
	private int nlot;
	
	@JsonProperty("e04")
	@JsonFormat(pattern = "YYYYMMDD")
	private Date datearrete;
	
	@JsonProperty("e05")
	@JsonFormat(pattern = "YYYY-MM-DD hh:mm:ss")
	private Date datecreation;
	
	@JsonProperty("ctr")
	private CtrDeatil[] ctrs;
	
	
	
}
