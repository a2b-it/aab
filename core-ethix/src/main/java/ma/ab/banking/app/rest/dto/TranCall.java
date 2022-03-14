/**
 * 
 */
package ma.ab.banking.app.rest.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author a.bouabidi
 *
 */
@Getter
@Setter
public class TranCall {
	
	
	private String acctDeb;
	
	private String acctCrd;
	
	private Double amount;
}
