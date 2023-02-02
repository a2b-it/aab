package ma.cam.jms.config;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.pcbsys.nirvana.client.nChannelAttributes;
import com.pcbsys.nirvana.client.nQueue;
import com.pcbsys.nirvana.client.nSession;
import com.pcbsys.nirvana.client.nSessionAttributes;
import com.pcbsys.nirvana.client.nSessionFactory;

import ma.cam.dto.ParamsApp;
import ma.cam.dto.ParamsQueue;
import ma.cam.jms.handler.message.exceptions.GlobalApplicationException;
import ma.cam.service.ParamsApplicationService;

@Configuration
public class ConnectionFactoryNirvana {
	
	Logger logger = LoggerFactory.getLogger(GlobalApplicationException.class);
	
	@Autowired
	private ParamsApplicationService	paramsApplicationService;
	
	@Autowired
	private GlobalApplicationException	applicationException;
	
	
	public static String RNAME	=null;
	public static String QUEUEOUT	=null;
	public static List<ParamsQueue> LISTQUEUE = new ArrayList<ParamsQueue>();
	public static List<ParamsApp> LISTPARAMS = new ArrayList<ParamsApp>();
	public static List<nQueue> LISTNQUEUE_LISTENER=new ArrayList<nQueue>();
	public static nSession SESSIONFACTORYAPI=null;
    	
	//Load params from DataBase
	public void loadParams () throws Exception{
		logger.info("____________________________Get all params from database_____________________________________________________");			
		try {
			logger.info("Start Get all params from database");
			LISTPARAMS = paramsApplicationService.getListParamsApp();				
		} catch (Exception e) {
			applicationException.fireTrace (e,"Error getting params from database !!!!","ConnectionFactoryNirvana", "loadParams");
			throw new Exception("Error getting params from database!!");
		}
		
		if(LISTPARAMS == null ||  LISTPARAMS.isEmpty()) {
			 applicationException.fireTrace (new Exception("PARAMS LIST"),"List PARAMS is empty !!!!","ConnectionFactoryNirvana", "loadParams");
			 throw new Exception("List PARAMS is empty !!!!");
		}
		
		for (ParamsApp parmsApp : LISTPARAMS) {
			if (parmsApp.getCode().equalsIgnoreCase("RNAME")) {
				RNAME=parmsApp.getValeur();
			}
		}
				
		try {
			logger.info("Start Get all QUEUES from database");
			LISTQUEUE = paramsApplicationService.getListQueues();			
		} catch (Exception e) {
			applicationException.fireTrace (new Exception("QUEUE LIST"),"Error Get all QUEUES from database!!!!","ConnectionFactoryNirvana", "loadParams");
			throw new Exception("Error Get all QUEUES from database!!!!");
		}
		
		if(LISTQUEUE == null ||  LISTQUEUE.isEmpty()) {
			applicationException.fireTrace (new Exception("Liste QUEUES empty"),"Liste QUEUES empty !!!!","ConnectionFactoryNirvana", "loadParams");
			throw new Exception("Liste QUEUES empty !!!!");
		}
		
		logger.info("-> Getting all params from database <OK>");	
	}
	
	@Bean
	@PostConstruct
	public nSession connectionNirvanaFactory() throws Exception{
		if (SESSIONFACTORYAPI != null && SESSIONFACTORYAPI.isConnected()) {
			return SESSIONFACTORYAPI;
		}
		
		if (RNAME==null || LISTQUEUE.isEmpty()) {
			try {
				loadParams();
			} catch (Exception e) {
				applicationException.fireTrace (new Exception("Error getting params or queues from database !!"),"Error getting params or queues from database !!","ConnectionFactoryNirvana", "loadParams");
				//throw new Exception("Error getting params or queues from database !!");
				return null;
			}
			
			if(LISTQUEUE == null ||  LISTQUEUE.isEmpty()) {
				applicationException.fireTrace (new Exception("Liste QUEUES empty"),"Liste QUEUES empty !!!!","ConnectionFactoryNirvana", "loadParams");
				//throw new Exception("Liste QUEUES empty !!!!");
				return null;
			}
		}
		
		logger.info("____________________________________<Start Load connection Factory Nirvana>_____________________________________________");
		logger.info("-> Start getting Connection Factory Rname :{} ",RNAME);
		nSessionAttributes sessionAttributes = null;
		nSession connectionObject = null;
		try {
			sessionAttributes = new nSessionAttributes(RNAME);
			connectionObject = nSessionFactory.create(sessionAttributes);
			connectionObject.init();
		} catch (Exception e) {			
			applicationException.fireTrace (e,"Unable to create session uri:"+RNAME+" !!!","ConnectionFactoryNirvana", "loadParams");
			//throw new SessionCreationException(e,"Unable to create session !!");
			return null;
		}
		
		SESSIONFACTORYAPI =  connectionObject;
		
		LISTNQUEUE_LISTENER=new ArrayList<nQueue>();
		for (ParamsQueue paramsQueue : LISTQUEUE) {
			if (paramsQueue.getInout().equalsIgnoreCase("OUT")) {
				QUEUEOUT 		= paramsQueue.getCodequeuescpi();
			}else {
				nChannelAttributes cattrib = new nChannelAttributes();
		        cattrib.setName(paramsQueue.getCodequeuescpi());
		        try {
		        	LISTNQUEUE_LISTENER.add(SESSIONFACTORYAPI.findQueue(cattrib) );
				} catch (Exception e) {
					applicationException.fireTrace (e,"Channel could not be found on the server:Find : {"+paramsQueue.getCode()+"}","ConnectionFactoryNirvana", "loadParams");
					//throw new SessionCreationException(e,"Channel could not be found on the server:Find : {"+paramsQueue.getCode()+"}");
					return null;
				}
				
			}
		}
		logger.info("-> Getting Connection Factory Rname {} -OK- ",RNAME);
		return connectionObject;
	}
}
