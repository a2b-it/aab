package ma.ab.banking.rest;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.ab.banking.services.TransactionEthixService;

@RestController
@AllArgsConstructor
@Slf4j
public class CoreEthixController {
	
	@Autowired
	private TransactionEthixService txEtxService; 
	
	@GetMapping("/exec")
    public void exec_virement()  {
		txEtxService.ExecuteTransactionSet("10000000038", Double.valueOf(1500.00));
		
		
	}
    
    @GetMapping("/get")
    public void startGetJob()  {
	
    
	}    
	
	

}
