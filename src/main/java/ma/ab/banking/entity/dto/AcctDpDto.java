/**
 * 
 */
package ma.ab.banking.entity.dto;



import lombok.Getter;
import lombok.Setter;

/**
 * @author a.bouabidi
 *
 */


@Getter
@Setter
public class AcctDpDto {
///a.acct_no, a.acct_type, a.appl_type, c.iso_code, 'DP' dpLn	

	//@Column(name = "ACCT_NO")
	private String acctNo;	
	

	//@Column(name = "ACCT_TYPE")
	private String acctType;	
	
	
	//@Column(name = "APPL_TYPE")
	private String applType;
	

	//@Column(name = "DPLN")
	private String dpLn ;
	

	//@Column(name = "iso_code")
	private String isoCode;
	

}
