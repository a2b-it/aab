package ma.cam.scheduling;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import com.pcbsys.nirvana.client.nConsumeEvent;
import com.pcbsys.nirvana.client.nQueue;

import ma.cam.dto.MessageCsm;
import ma.cam.jms.config.ConnectionFactoryNirvana;
import ma.cam.jms.config.UniversalMessagingInterface;
import ma.cam.jms.handler.message.exceptions.ActionNotPossibleException;
import ma.cam.jms.handler.message.exceptions.GlobalApplicationException;
import ma.cam.jms.handler.message.exceptions.CannotSendToQueueException;
import ma.cam.model.MessageOracle;
import ma.cam.service.MessageRecuService;
import ma.cam.tools.StringTools;

@Service
public class JobCam {
	Logger logger = LoggerFactory.getLogger(JobCam.class);
		
	private static final   String CH_IDEVENEMENT	="V_IDEVENEMENT";
	private static final   String CH_TAG			="V_TAG";
	private static final   String CH_FLUX_CLOB		="V_FLUX_CLOB";
	@Autowired
	private GlobalApplicationException	applicationException;
	
	@Autowired
	UniversalMessagingInterface	universalMessagingInterface;
	
	@Autowired
	MessageRecuService	messageRecuService;
	
	public JobCam() {
		
		logger.info("__________________ Start JOB read from database event (GE) ________________");
		String cronExpression = "0/2 * * * * *";
	    ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
	    scheduler.setPoolSize(30);
	    scheduler.setThreadFactory(new ThreadPoolTaskExecutor());
	    scheduler.initialize();
	    
		Runnable taskJobReadFromDatabase = () -> {
			//logger.info("__________________ JOB CAM {} ________________", new Date());
			Long idevenement = null;
			String tag = null;
			String messageId = null;
			String fluxClob = null;
			String flagExists = null;
			MessageOracle msg = null;
			List<MessageCsm> listFluxOut;
			String statutSend = null;
			nConsumeEvent evtSended;
			
			//Call : PR_GET_UN_FLUX_CLOB_SORTANT
			if (ConnectionFactoryNirvana.SESSIONFACTORYAPI == null) {
				applicationException.fireTrace (new Exception("Session unavailable  !!"),"Session unavailable  !!","JobCam", "JobCam");
				return;
			}
			
            try {
        		listFluxOut = messageRecuService.prGetListeFluxClobSortant();
            } catch (Exception e) {
				logger.error("Erreur => Call : PR_GET_UN_FLUX_CLOB_SORTANT : {}" ,e.getMessage());
				applicationException.fireTrace (e,"Erreur => Call : PR_GET_UN_FLUX_CLOB_SORTANT :"+e.getMessage(),"JobCam", "loadParams");
				return;
			}	
            if (listFluxOut==null || listFluxOut.isEmpty()) {
            	return;
            }
			for (MessageCsm messageCsm : listFluxOut) {
					// Vérifier si flag Exists = 'O'
					try {
						fluxClob = messageCsm.getFluxclob() ;
					} catch (Exception e) {
						logger.info("-> Exception Read fluxClob {}", e.getMessage());
						applicationException.fireTrace(e, "Erreur => Read fluxClob :" + e.getMessage(), "JobCam",
								"loadParams");
						return;
					}

					try {
						tag = messageCsm.getTag();
					} catch (Exception e) {
						logger.info("-> Exception Read tag: {}", e.getMessage());
						applicationException.fireTrace(e, "Erreur => Read tag :" + e.getMessage(), "JobCam",
								"loadParams");
						return;
					}
					
					try {
						messageId = messageCsm.getMessageid();
					} catch (Exception e) {
						logger.info("-> Exception Read messageId: {}", e.getMessage());
						applicationException.fireTrace(e, "Erreur => Read messageId :" + e.getMessage(), "JobCam",
								"loadParams");
						return;
					}

					try {
						idevenement = messageCsm.getIdentifiant();
					} catch (Exception e) {
						logger.info("-> Exception Read idevenement: {}", e.getMessage());
						applicationException.fireTrace(e, "Erreur => Read idevenement :" + e.getMessage(), "JobCam",
								"loadParams");
						return;
					}

					logger.info("JOB CAM => Id event : {}", idevenement);
					logger.info("JOB CAM => Tag  : {}", tag);
					logger.info("JOB CAM => Flux Clob : {}", fluxClob);					

					nQueue queueOut = null;
					try {
						queueOut = universalMessagingInterface.getQueue(ConnectionFactoryNirvana.QUEUEOUT);
					} catch (ActionNotPossibleException e) {
						applicationException.fireTrace(e, "Erreur => Exception get Queue :" + e.getMessage(), "JobCam",
								"loadParams");
						logger.error("-> Exception get Queue: {}", ConnectionFactoryNirvana.QUEUEOUT);
						return;
					}
					
					logger.info("queueOut :",queueOut);
					
					if (tag==null || queueOut == null ||fluxClob==null ) {
						applicationException.fireTrace(new Exception("values is null"),
								"Erreur => Sending message to  :" + queueOut + " - tag:" + tag + " - fux : " + fluxClob,
								"JobCam", "loadParams");
						try {
							msg = messageRecuService.prSetEtatEnvoiCsm(idevenement.toString(), "N","values is null", null);
						} catch (Exception e1) {
							applicationException.fireTrace(new Exception("values is null"), "Erreur => PR_SET_ETAT_ENVOI_CSM " + e1.getMessage(),
									"JobCam", "loadParams");
							logger.error("Erreur => Call : PR_SET_ETAT_ENVOI_CSM : {}", e1.getMessage());
							
						}
						
						
					}else {
					
					try {
						evtSended = universalMessagingInterface.sendMessagesToQueue(queueOut, tag, fluxClob,messageId);
						
					} catch (CannotSendToQueueException e) {
						applicationException.fireTrace(e,
								"Erreur => Sending message to  :" + queueOut + " - tag:" + tag + " - fux : " + fluxClob,
								"JobCam", "loadParams");
						e.printStackTrace();
						try {
							msg = messageRecuService.prSetEtatEnvoiCsm(idevenement.toString(), "N", e.getMessage(), null);
						} catch (Exception e1) {
							applicationException.fireTrace(e, "Erreur => PR_SET_ETAT_ENVOI_CSM " + e1.getMessage(),
									"JobCam", "loadParams");
							logger.error("Erreur => Call : PR_SET_ETAT_ENVOI_CSM : {}", e1.getMessage());
							return;
						}
						return;
					}

					try {
						msg = messageRecuService.prSetEtatEnvoiCsm(idevenement.toString(), "O", null,
								StringTools.getSHA256Hash(fluxClob));
					} catch (Exception e) {
						applicationException.fireTrace(e, "Erreur => PR_SET_ETAT_ENVOI_CSM " + e.getMessage(), "JobCam",
								"loadParams");
						logger.error("Erreur => Call : PR_SET_ETAT_ENVOI_CSM : {}", e.getMessage());
						return;
					}        		
    		}
			}
			logger.info("-> job already in progress: {}" ,new Date());
		};
		
		logger.info("-> Job CAM Execut: {}" ,new Date());
		//scheduler.setPoolSize(10);
		scheduler.schedule(taskJobReadFromDatabase,new CronTrigger(cronExpression));
	}
	
}
