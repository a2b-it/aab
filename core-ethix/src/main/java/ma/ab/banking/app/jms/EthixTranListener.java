/**
 * 
 */
package ma.ab.banking.app.jms;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/**
 * @author a.bouabidi
 *
 */
@Component
public class EthixTranListener {

	
  @JmsListener(destination = "ETHIX_TRAN_Q", containerFactory = "aqApiFactory")
  public void receiveMessage(String message) {
    System.out.println("Received <" + message + ">");
  }
}
