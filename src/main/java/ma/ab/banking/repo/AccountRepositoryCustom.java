/**
 * 
 */
package ma.ab.banking.repo;

import java.util.Date;

import ma.ab.banking.entity.dto.AcctDpDto;

/**
 * @author a.bouabidi
 *
 */
public interface AccountRepositoryCustom {
	public AcctDpDto customNativeQuery (String acctNum);
	
	public Date findByNativeQueryGetCurrentJounee ();
}
