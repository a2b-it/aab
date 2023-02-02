/**
 * 
 */
package ma.ab.banking.app.jms;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import ma.ab.banking.app.rest.dto.JmsTranMsg;

/**
 * @author a.bouabidi
 *
 */
@Slf4j
@Component
public class EthixTranListener implements MessageListener{

	/*
  @JmsListener(destination = "ETHIX_TRAN_Q", containerFactory = "aqApiFactory")
  public void receiveMessage(String message) {
    System.out.println("Received <" + message + ">");
  }*/

	@Override
	public void onMessage(Message message) {
		String text="";
		try {
			ObjectMapper oM = new ObjectMapper();
			
			text = ((TextMessage) message).getText();
			JmsTranMsg t =oM.readValue(text, JmsTranMsg.class);
			
			log.info("Received <" + t + ">");
		} catch (JMSException | JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       
		
	}
}
