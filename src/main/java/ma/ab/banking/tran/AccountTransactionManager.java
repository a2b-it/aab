/**
 * 
 */
package ma.ab.banking.tran;

import ma.ab.banking.entity.AcctTransaction;
import ma.ab.banking.entity.AcctTransactionSet;
import ma.ab.banking.tran.excep.TransactionException;

/**
 * @author a.bouabidi
 *
 */
public interface AccountTransactionManager {
	
	AcctTransactionSet execute (AcctTransactionSet set) throws TransactionException;
	
	
	AcctTransaction execute (AcctTransaction tran) throws TransactionException;
	

}

