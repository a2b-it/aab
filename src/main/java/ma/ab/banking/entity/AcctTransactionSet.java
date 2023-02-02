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
public class AcctTransactionSet {
	
	private AcctTransaction[] trans;
	
	private String name;
	
	private Long id;
	
	private String code;
		
	private String description;
}
