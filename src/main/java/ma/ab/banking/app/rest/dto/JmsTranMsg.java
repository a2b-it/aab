/**
 * 
 */
package ma.ab.banking.app.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

/**
 * @author a.bouabidi
 *
 */
@Getter
@Setter
public class JmsTranMsg {
	
	private String sender;
	
	@JsonProperty("corl_id")
	private String corlId;
	
	@JsonProperty("tran_id")
	private String tranId;
	
	private String codeop;
	
	private String cptd;
	
	private String cptc;
	
	private String mt;
}
