package ma.ab.banking.app.rest;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.ab.banking.app.services.TransactionEthixService;
import ma.ab.banking.tran.excep.TransactionException;
import ma.ab.banking.tran.excep.TransactionValidationException;


@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping(value = "/")
public class CoreEthixController {
	
	@Autowired
	private TransactionEthixService txEtxService; 
	
	@GetMapping("/exec")
    public ResponseEntity<String> exec_virement(String numCptDebit, String numCptCredit, Double montant)  {
		long n;
		try {
			n = txEtxService.ExecuteTransactionSet("10000000038", "10000000034",Double.valueOf(1500.00));
		} catch (TransactionValidationException e) {
			return ResponseEntity.ok(e.getMessage());
		} catch (TransactionException e) {
			return ResponseEntity.ok(e.getMessage());
		}
		
		return ResponseEntity.ok("Return value"+n);
	}
    
    @GetMapping("/get")
    public void startGetJob()  {
	
    
	}    
	
	

}
