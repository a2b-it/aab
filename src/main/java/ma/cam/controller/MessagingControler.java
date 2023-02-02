package ma.cam.controller;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pcbsys.nirvana.client.nChannelAttributes;
import com.pcbsys.nirvana.client.nChannelNotFoundException;
import com.pcbsys.nirvana.client.nConsumeEvent;
import com.pcbsys.nirvana.client.nQueue;
import com.pcbsys.nirvana.client.nQueueSyncTransactionReader;
import com.pcbsys.nirvana.client.nRealmUnreachableException;
import com.pcbsys.nirvana.client.nSession;

import ma.cam.jms.config.ConnectionFactoryNirvana;
import ma.cam.jms.config.UniversalMessagingInterface;
import ma.cam.jms.handler.message.exceptions.ActionNotPossibleException;
import ma.cam.jms.handler.message.exceptions.GlobalApplicationException;
import ma.cam.jms.handler.message.exceptions.CannotSendToQueueException;
import ma.cam.model.MessageOracle;
import ma.cam.scheduling.JobCam;
import ma.cam.service.MessageRecuService;
import ma.cam.tools.P2P;
import ma.cam.tools.StringTools;
import ma.cam.tools.TraceText;

@RestController
@RequestMapping("/messaging")
public class MessagingControler {
	Logger logger = LoggerFactory.getLogger(MessagingControler.class);
	
	private static final   String CH_FLAGEXISTS		="V_FLAGEXISTS";
	private static final   String CH_IDEVENEMENT	="V_IDEVENEMENT";
	private static final   String CH_TAG			="V_TAG";
	private static final   String CH_FLUX_CLOB		="V_FLUX_CLOB";
	private Long i=0L;
	@Autowired
	private MessageRecuService messageRecuService;
	
	@Autowired
	private P2P	p2p;
	
	@Autowired
	private TraceText traceText;
	
	@GetMapping("/send")
	public String sendMessage (){
		traceText = new TraceText();
		String textTrace="";
		textTrace="--------------------test ("+(i++) +")--------------------------------";
		textTrace+="<br /> = Date : "+new Date();
		
		textTrace+="<br />Send message to SCPI";
		
		
		//logger.info("__________________ JOB CAM {} ________________", new Date());
		String idevenement = null;
		String port = null;
		String url = null;
		String virement = null;
		String queueName = null;
		String tag = null;
		String fluxClob = null;
		String flagExists = null;
		MessageOracle msg = null;
		
		
		//Call : PR_GET_UN_FLUX_CLOB_SORTANT
        try {
    		msg 			= messageRecuService.prSimulationGetUnFluxClobSortant();
        } catch (Exception e) {
			logger.error("Erreur => Call : PR_GET_UN_FLUX_CLOB_SORTANT : {}" ,e.getMessage());
			textTrace+="<br />"+"Erreur => Call : PR_GET_UN_FLUX_CLOB_SORTANT : "+e.getMessage();
			return textTrace;
		}	
        
        try {            	
            flagExists 	= msg.getRetours().get(CH_FLAGEXISTS).toString();
		} catch (Exception e) {
			logger.error("Erreur => Read V_FLAGEXISTS : {}" ,e.getMessage());
			textTrace+="<br />"+"Erreur => Read V_FLAGEXISTS: "+e.getMessage();
			return textTrace;
		}
        
        if (flagExists.equalsIgnoreCase("N")) {
        	logger.info("No message to send" );
			//textTrace+="<br />"+"<span style=\"color:red\">No message to send</span>";
        	traceText.addTraceElement("No message to send", "B");        	
			return textTrace+"<br />"+traceText.toString();
        }
		try {
			fluxClob 		= msg.getRetours().get(CH_FLUX_CLOB).toString();
			logger.info("fluxClob  => {}" ,fluxClob);
		} catch (Exception e2) {
			logger.error("-> Exception Read fluxClob {}",e2.getMessage() );
			textTrace+="<br />"+"Erreur => Exception Read fluxClob : "+e2.getMessage();
			return textTrace;
		}
		
		
		try {
			tag 		= msg.getRetours().get(CH_TAG).toString();
		} catch (Exception e2) {
			logger.error("-> Exception Read tag: {}" );
			textTrace+="<br />"+"Erreur => Exception Read tag : "+e2.getMessage();
			return textTrace;
		}
		
		try {
			idevenement 	= msg.getRetours().get(CH_IDEVENEMENT).toString();
		} catch (Exception e2) {
			logger.error("-> Exception Read idevenement: {}" );
			textTrace+="<br />"+"Erreur => Exception Read idevenement: "+e2.getMessage();
			return textTrace;
		}
		
		try {
			port 	= msg.getRetours().get("V_PORT").toString();
		} catch (Exception e2) {
			logger.error("-> Exception Read port: {}" );
			textTrace+="<br />"+"Erreur => Exception Read port: "+e2.getMessage();
			return textTrace;
		}
		
		try {
			url 	= msg.getRetours().get("V_URL").toString();
		} catch (Exception e2) {
			logger.error("-> Exception Read url: {}" );
			textTrace+="<br />"+"Erreur => Exception Read url: "+e2.getMessage();
			return textTrace;
		}
		
		try {
			virement 	= msg.getRetours().get("V_IDVIREMENT").toString();
		} catch (Exception e2) {
			logger.error("-> Exception Read id virement: {}" );
			textTrace+="<br />"+"Erreur => Exception Read  id virement: "+e2.getMessage();
			return textTrace;
		}
		
		try {
			queueName 	= msg.getRetours().get("V_QUEUE").toString();
		} catch (Exception e2) {
			logger.error("-> Exception Read id virement: {}" );
			textTrace+="<br />"+"Erreur => Exception Read queue name: "+e2.getMessage();
			return textTrace;
		}
		
		logger.info("PORT : {}" ,port);
		logger.info("URI : {}" ,url);
		logger.info("QUEU  : {}" ,queueName);
		logger.info("id virement : {}" ,virement);
		logger.info("Id event : {}" ,idevenement);
		logger.info("Tag  : {}" ,tag);
		logger.info("Flux Clob V1 : {}" ,fluxClob);
		logger.info("FLAG EXISTS : {}" ,flagExists);
		
		nQueue queueOut = null;
		nSession sessionUm;
		try {
			sessionUm = p2p.connect(url+port);
		} catch (nRealmUnreachableException e) {
			logger.error("-> Realm is currently not reachable url {} => Exception {}" ,url+port,e.getMessage());
			textTrace+="<br />"+"Realm is currently not reachable url {: "+url+port+"} Exception : {"+e.getMessage()+"}";
			return textTrace;
		} catch (Exception e) {
			logger.error("-> Exception get Session url {} => Exception {}" ,url+port,e.getMessage());
			textTrace+="<br />"+"-> Exception get Session url {"+url+port+"} => Exception {"+e.getMessage()+"}";
			return textTrace;
		}
		
		try {
			queueOut = p2p.getQueue(sessionUm, queueName);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("-> Exception get Queue: {}" ,queueName);
			textTrace+="<br />"+"Exception get Queue : "+queueName;
			return textTrace;
		}
		
		textTrace+="<br />"+"getQueue (OK) : "+ConnectionFactoryNirvana.QUEUEOUT;
		
		try {
			p2p.send(fluxClob, tag, queueOut);
		} catch (Exception e) {
			e.printStackTrace();
			try {
        		msg = messageRecuService.prSetEtatEnvoiCsm(idevenement,"N",e.getMessage(),null);
            } catch (Exception e1) {
				logger.error("Erreur => Call : PR_SET_ETAT_ENVOI_CSM : {}" ,e1.getMessage());
				textTrace+="<br />"+"Exception get Queue : "+ConnectionFactoryNirvana.QUEUEOUT;
				return textTrace;
			}	
			
			logger.error("Erreur => sendMessagesToQueue : {}" ,e.getMessage());
			textTrace+="<br />"+"Erreur => sendMessagesToQueue :"+e.getMessage();
			return textTrace;
			
		}
		
		textTrace+="<br />"+"Send Messages To Queue (OK) ";
		
		try {
    		msg = messageRecuService.prSetEtatEnvoiCsm(idevenement,"O",null, StringTools.getSHA256Hash(fluxClob));
        } catch (Exception e) {
			logger.error("Erreur => Call : PR_SET_ETAT_ENVOI_CSM : {}" ,e.getMessage());
			textTrace+="<br />"+"Erreur => Call : PR_SET_ETAT_ENVOI_CSM :"+e.getMessage();
			return textTrace;
		}
		
		textTrace+="<br />"+ 
				"Port :"+port
				+"<br />URI:"+url
				+"<br />URI connectionFactory:"+url+port
				+"<br />Id Queue Name:"+queueName
				+"<br />Id event:"+idevenement
				+"<br />Id Virement:"+virement
				+"<br />Tag:"+tag
				+"<br />Flux clob:"+fluxClob
				+"<br />Flag exists:"+flagExists
				;
	   return textTrace;
	}
	
	
	@GetMapping("/receive")
	public String getAllErrorTrace (){
		traceText = new TraceText();
		//String textTrace="";
		traceText.addTraceElement("--------------------test ("+(i++)+")--------------------------------","B");
		traceText.addTraceElement("Date : "+new Date(),"B");
		traceText.addTraceElement("Receive message from SCPI","B");
		
		String port = null;
		String url = null;
		String queue = null;
		String nomprocedure = null;
		
		MessageOracle msg = null;
		
		//Call : PR_GET_UN_FLUX_CLOB_SORTANT
        try {
    		msg 			= messageRecuService.prGetInfoReceive();
        } catch (Exception e) {
			logger.error("Erreur => Call : PR_GET_INFO_RECIEVE : {}" ,e.getMessage());
			traceText.addTraceElement("Call : PR_GET_INFO_RECIEVE : "+e.getMessage(),"B");
			return traceText.toString();
		}
        
        try {
			port 	= msg.getRetours().get("V_PORT").toString();
		} catch (Exception e2) {
			logger.error("-> Exception Read port: {}" );
			traceText.addTraceElement("Exception Read port : "+e2.getMessage(),"R");
			return traceText.toString();
		}
		
		try {
			url 	= msg.getRetours().get("V_URL").toString();
		} catch (Exception e2) {
			logger.error("-> Exception Read url: {}" );
			traceText.addTraceElement("Exception Read url : "+e2.getMessage(),"R");
			return traceText.toString();
		}
		
		try {
			queue 	= msg.getRetours().get("V_QUEUE").toString();
		} catch (Exception e2) {
			logger.error("-> Exception Read url: {}" );
			traceText.addTraceElement("Exception Read queue : "+e2.getMessage(),"R");
			return traceText.toString();
		}
		
		try {
			nomprocedure 	= msg.getRetours().get("V_NOMPROCEDURE").toString();
		} catch (Exception e2) {
			logger.error("-> Exception Read url: {}" );
			traceText.addTraceElement(" Exception Read nom procedure: "+e2.getMessage(),"R");
			return traceText.toString();
		}
		
		logger.info("PORT : {}" ,port);
		logger.info("URI : {}" ,url);
		logger.info("Queue : {}" ,queue);
		logger.info("URL : {}" ,url+port);

		
		traceText.addTraceElement("Port :"+port, "B");
		traceText.addTraceElement("URI:"+url, "B");
		traceText.addTraceElement("URI connectionFactory:"+url+port, "B");
		traceText.addTraceElement("Id Queue Name:"+queue, "B");
		traceText.addTraceElement("Id Nom Procedure:"+nomprocedure, "B");
		
		
		nQueue queueIn = null;
		nSession sessionUm;
		try {
			sessionUm = p2p.connect(url+port);
		} catch (nRealmUnreachableException e) {
			logger.error("-> Realm is currently not reachable url {} => Exception {}" ,url+port,e.getMessage());
			traceText.addTraceElement("Realm is currently not reachable url {: "+url+port+"} Exception : {"+e.getMessage()+"}", "R");
			return traceText.toString();
		} catch (Exception e) {
			logger.error("-> Exception get Session url {} => Exception {}" ,url+port,e.getMessage());
			traceText.addTraceElement("Exception get Session url {"+url+port+"} => Exception {"+e.getMessage()+"}", "R");
			return traceText.toString();
		}
		
		try {
			queueIn = p2p.getQueue(sessionUm, queue);
		} catch (Exception e) {
			logger.info("-> Exception get Queue: {}- Exception :{}" ,queue,e.getMessage());
			traceText.addTraceElement("Exception get Queue: {"+queue+"}- Exception : "+e.getMessage(), "R");
			return traceText.toString();
		}
		
		nConsumeEvent recievedMessage=null;
		try {
			recievedMessage = p2p.receive(queueIn);
		} catch (Exception e) {
			logger.info("-> Exception receive: {}- Exception :{}" ,queue,e.getMessage());
			traceText.addTraceElement("Exception receive: {"+queue+"}- Exception : "+e.getMessage(), "R");
			return traceText.toString();
		}
		
		long id = recievedMessage.getEventID();
		String tag = recievedMessage.getEventTag();
		byte[] data = recievedMessage.getEventData();
		String message = new String(data, StandardCharsets.ISO_8859_1);
		
		//return "Message taggé " + tag + " contenant <" + message + ">";
		
		traceText.addTraceElement("Received Message ID :"+id, "B");
		traceText.addTraceElement("Received Message TAG:"+tag, "B");
		traceText.addTraceElement("Received Message TEXT:"+message, "B");

		try {
			msg = messageRecuService.prSetMessageRecu(nomprocedure ,id+"", tag, message, null);
		} catch (Exception e) {
			logger.info("-> Exception call: {}- Exception :{}" ,nomprocedure,e.getMessage());
			traceText.addTraceElement("Exception call: {"+nomprocedure+"}- Exception : "+e.getMessage(), "R");
			return traceText.toString();
		}
		return traceText.toString();
	}
}
