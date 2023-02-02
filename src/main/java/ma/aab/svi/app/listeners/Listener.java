/**
 * 
 */
package ma.aab.svi.app.listeners;

import javax.jms.Message;
import javax.jms.MessageListener;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/**
 * @author a.bouabidi
 *
 */
@Component
public class Listener implements MessageListener {

	@Override
	public void onMessage(Message message) {
		System.out.println("Received <" + message + ">");
		
	}

	/*
	 @JmsListener(destination = "ETHIX_TRAN_Q")
	  public void receiveMessage(String message) {
	    System.out.println("Received <" + message + ">");
	  }*/
}
