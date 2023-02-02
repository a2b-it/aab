/**
 * 
 */
package ma.ab.banking.app.entity;



import lombok.Getter;
import lombok.Setter;

/**
 * @author a.bouabidi
 *
 */


@Getter
@Setter
public class AcctDpDto {
	
	private final String STATUS_ACTIVE="Active";

	//@Column(name = "ACCT_NO")
	private String acctNo;	
	

	//@Column(name = "ACCT_TYPE")
	private String acctType;	
	
	
	//@Column(name = "APPL_TYPE")
	private String applType;
	

	//@Column(name = "DPLN")
	private String dpLn ;
	

	private Integer branchno;
	
	private String titulaire;
	
	private String status;
	//@Column(name = "iso_code")
	private String isoCode;
	

}
