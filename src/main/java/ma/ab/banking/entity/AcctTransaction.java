/**
 * 
 */
package ma.ab.banking.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * @author a.bouabidi
 *
 */
@Getter
@Setter
public class AcctTransaction {
	
	public static enum SENS {
		Debit,
		Credit
	}
	
	public static enum STATUS {
		initiated,// preparing data
		validated,// validate data and account type
		executing,//executing on core banking		
		rejected,//rejected by core banking
		accepted,//accepted by core banking
		failed, // exception during execution
		aborted// Aborted  
		
	}
	
	private Double amount;
	
	private String debAcctType;
	
	private String crdAcctType;
	
	private SENS sens; 
	
	private Long id;
	
	private String code;
	
	private STATUS status;
	
	private String refNum;
	

}
