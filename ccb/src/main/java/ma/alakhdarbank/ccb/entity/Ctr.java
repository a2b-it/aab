/**
 * 
 */
package ma.alakhdarbank.ccb.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

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
	@GeneratedValue(strategy=GenerationType.AUTO)
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
	@JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
	private Date datecreation;
	
	@JsonProperty("ctr")
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "ctr_id")
	private List<CtrDetail> ctrs;
	
	
	
}
