package ma.cam.jms.handler.message.listener;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Service;

import com.pcbsys.nirvana.client.nChannelAttributes;
import com.pcbsys.nirvana.client.nConsumeEvent;
import com.pcbsys.nirvana.client.nEventListener;
import com.pcbsys.nirvana.client.nIllegalArgumentException;
import com.pcbsys.nirvana.client.nQueue;
import com.pcbsys.nirvana.client.nQueueReaderContext;
import com.pcbsys.nirvana.client.nQueueSyncReader;
import com.pcbsys.nirvana.client.nQueueSyncTransactionReader;
import com.pcbsys.nirvana.client.nSession;

import ma.cam.dto.ParamsQueue;
import ma.cam.jms.config.ConnectionFactoryNirvana;
import ma.cam.jms.config.UniversalMessagingInterface;
import ma.cam.jms.handler.message.exceptions.GlobalApplicationException;
import ma.cam.model.MessageOracle;
import ma.cam.service.MessageRecuService;

@Service
@Import({ ConnectionFactoryNirvana.class, GlobalApplicationException.class })
public class ListenerQueueCamGsimt implements nEventListener {
	@Autowired
	MessageRecuService messageRecuService;

	@Autowired
	UniversalMessagingInterface universalMessagingInterface;

	@Autowired
	private GlobalApplicationException applicationException;

	Logger logger = LoggerFactory.getLogger(ListenerQueueCamGsimt.class);

	nChannelAttributes cattrib;

	private nSession connectionObject;
	private ParamsQueue currentQueueEvent;
	private List<nQueue> listQueue;

	public ListenerQueueCamGsimt() {

		logger.info("____________________________________<Start listener>_____________________________________________");
		logger.info("Verify Session is valide");
		if (ConnectionFactoryNirvana.SESSIONFACTORYAPI == null) {
			logger.error("Error while Getting Session value");
			return;
		}

		try {
			connectionObject = ConnectionFactoryNirvana.SESSIONFACTORYAPI;
		} catch (Exception e) {
			applicationException.fireTrace(e, "Error getting session :", "ListenerQueueCamGsimt", "loadParams");
			return;
		}

		logger.info("Verify Session is valide  {OK}");

		try {
			listQueue = ConnectionFactoryNirvana.LISTNQUEUE_LISTENER;
		} catch (Exception e) {
			applicationException.fireTrace(e, "Error getting session !!!!", "ListenerQueueCamGsimt", "loadParams");
			return;
		}

		if (listQueue == null || listQueue.isEmpty()) {
			applicationException.fireTrace(new Exception("List Queues is empty"), "List Queues is empty !!!!","ListenerQueueCamGsimt", "loadParams");
			return;
		}

		logger.info("Find All Queue & start listener for each one");

		for (nQueue queueListener : listQueue) {
			nQueueReaderContext ctx = new nQueueReaderContext(this);
			try {
				queueListener.createAsyncReader(ctx);
			} catch (Exception e) {
				applicationException.fireTrace(e,
						"Exception start listener on queue {" + queueListener.getName() + "} !!!!",
						"ListenerQueueCamGsimt", "loadParams");
				return;
			}
		}

		logger.info("Starting listener {OK}");
	}

	@Override
	public void go(nConsumeEvent evt) {
		/*ExecutorService executorService = Executors.newFixedThreadPool(5);
		executorService.execute(new Runnable() {
			@Override
			public void run() {*/
				LocalDateTime now = LocalDateTime.now();
				nQueue queuecurrent=null;
				nQueueSyncReader reader = null;
				
				long id ; //id evenement au niveau queue 
				String tag = null;
				byte[] data = null;
				String message = null;
				MessageOracle msg = null;
				String idEvenementReponse=null;
				String flagRepondreCsm=null;
				String tagOut=null;
				String fluxClobOut=null;
				
				String messageName;
				String messageId = null;
				String checksum;
				String depositTimestamp;
				
				
				try {
					currentQueueEvent = universalMessagingInterface.getQueueParams(evt.getChannelName());
					cattrib = new nChannelAttributes();
					cattrib.setName(currentQueueEvent.getCodequeuescpi());
					queuecurrent = connectionObject.findQueue(cattrib);
				} catch (Exception e) {
					applicationException.fireTrace(e,
							"Exception reading message from queue {" + queuecurrent.getName() + "} !!!!",
							"Thread", "Reading message from queue");
					return;
				}
					
				try {
					reader = queuecurrent.createTransactionalReader(new nQueueReaderContext());
				} catch (nIllegalArgumentException e) {
					applicationException.fireTrace(e,
							"Exception reading message from queue {" + queuecurrent.getName() + "} !!!!",
							"Thread", "Reading message from queue");
					return;
				}
				
				if (evt != null) {
					id = evt.getEventID();
					tag = evt.getEventTag();
					data = evt.getEventData();
					
					
					
					try {
						messageName = evt.getProperties().get("messageName").toString();
					} catch (Exception e) {
						e.printStackTrace();
						applicationException.fireTrace(e,
								"Exception reading message from queue {" + queuecurrent.getName() + "},id:{"+messageId+" }, message :{"+message+"}, Tag :{"+tag+"}",
								"Thread", "cant read messageName property");
						logger.error("Error listenere queue : {} erreur : {}, message: {}, id: {}, tag: {}", evt.getChannelName(), e.getMessage(),message,messageId,tag);
						return;
					}
					
					try {
						checksum = evt.getProperties().get("checksum").toString();
					} catch (Exception e) {
						e.printStackTrace();
						applicationException.fireTrace(e,
								"Exception reading message from queue {" + queuecurrent.getName() + "},id:{"+messageId+" }, message :{"+message+"}, Tag :{"+tag+"}",
								"Thread", "cant read checksum property");
						logger.error("Error listenere queue : {} erreur : {}, message: {}, id: {}, tag: {}", evt.getChannelName(), e.getMessage(),message,messageId,tag);
						return;
					}
					
					try {
						depositTimestamp = evt.getProperties().get("depositTimestamp").toString();
					} catch (Exception e) {
						//e.printStackTrace();
						applicationException.fireTrace(e,
								"Exception reading message from queue {" + queuecurrent.getName() + "},id:{"+messageId+" }, message :{"+message+"}, Tag :{"+tag+"}",
								"Thread", "cant read depositTimestamp property");
						logger.error("Error listenere queue : {} erreur : {}, message: {}, id: {}, tag: {}", evt.getChannelName(), e.getMessage(),message,messageId,tag);
						//return;
					}
						
					try {
						messageId = evt.getProperties().get("messageID").toString();
					} catch (Exception e) {
						//e.printStackTrace();
						applicationException.fireTrace(e,
								"Exception reading message from queue {" + queuecurrent.getName() + "},id:{"+messageId+" }, message :{"+message+"}, Tag :{"+tag+"}",
								"Thread", "cant read messageID property");
						logger.error("Error listenere queue : {} erreur : {}, message: {}, id: {}, tag: {}", evt.getChannelName(), e.getMessage(),message,messageId,tag);
						//return;
					}
					
					
					try {
						((nQueueSyncTransactionReader) reader).commit(id);
					} catch (Exception e) {
						e.printStackTrace();
						applicationException.fireTrace(e,
								"Exception reading message from queue {" + queuecurrent.getName() + "},id:{"+messageId+" }, message :{"+message+"}, Tag :{"+tag+"}",
								"Thread", "Reading message from queue");
						logger.error("Error listenere queue : {} erreur : {}, message: {}, id: {}, tag: {}", evt.getChannelName(), e.getMessage(),message,messageId,tag);
						return;
					}
					
					message = new String(data, StandardCharsets.ISO_8859_1);
					
					try {
						msg = messageRecuService.prSetMessageRecu(currentQueueEvent.getNomprocedure(),messageId + "", tag, message, null);						
					} catch (Exception e) {
						applicationException.fireTrace(e, "Error listenere queue : {" + evt.getChannelName()
								+ "} erreur : {" + e.getMessage() + "}", "ListenerQueueCamGsimt", "loadParams");
						logger.error("Error call back  PR_SET_MESSAGE_RECU : {} erreur : {}, message: {}, id: {}, tag: {}", evt.getChannelName(), e.getMessage(),message,messageId,tag);
						return;
					}
					
					flagRepondreCsm = msg.getRetours().get("V_FLAGREPONDRECSM").toString();
//					if (flagRepondreCsm.equalsIgnoreCase("O")) {
//						logger.info("_________________________ Send response (Start) _________________________");
//						try {
//							idEvenementReponse = msg.getRetours().get("V_IDEVENEMENTREPONSE").toString();
//							logger.info("FLAG EXISTS response = {}", msg.getRetours().get("V_FLAGREPONDRECSM"));
//							logger.info("Id event : {}", msg.getRetours().get("V_IDEVENEMENTREPONSE"));							
//
//						} catch (Exception e) {
//							applicationException.fireTrace(e, "Error listenere queue : {" + evt.getChannelName()
//									+ "} erreur : {" + e.getMessage() + "}", "ListenerQueueCamGsimt", "loadParams");
//							logger.error(
//									"Error call back  PR_SET_MESSAGE_RECU : {} erreur : {}, message: {}, id: {}, tag: {}",
//									evt.getChannelName(), e.getMessage(), message, id, tag);
//							return;
//						}
//						logger.info("line : {}", 1);
//						try {
//							msg = messageRecuService.prGetFluxClobSByIdeven(idEvenementReponse);
//							logger.info("line : {}", 1.1);
//						} catch (Exception e) {
//							applicationException.fireTrace(e, "Error listenere queue : {" + evt.getChannelName()
//									+ "} erreur : {" + e.getMessage() + "}", "ListenerQueueCamGsimt", "loadParams");
//							logger.error("Error call back  PR_GET_FLUX_CLOB_S_BY_IDEVEN : {} erreur : {}, message: {}, id: {}, tag: {}, idEvenementReponse {}", 
//									evt.getChannelName(), e.getMessage(),message,id,tag,idEvenementReponse);
//							return;
//						}
//						
//						logger.info("line : {}", 2);
//						try {
//							tagOut = msg.getRetours().get("V_TAG_OUT").toString();											
//						} catch (Exception e) {
//							applicationException.fireTrace(e, "Error callback prGetFluxClobSByIdeven : TAG null , id event: {" + idEvenementReponse
//									+ "} erreur : {" + e.getMessage() + "}", "ListenerQueueCamGsimt", "loadParams");
//							logger.error(
//									"Error callback prGetFluxClobSByIdeven : TAG null : {} erreur : {}, message: {}, id: {}, tag: {}",
//									idEvenementReponse, e.getMessage(), null, null, null);
//							return;
//						}
//						logger.info("line : {}", 3);
//						try {
//							fluxClobOut = msg.getRetours().get("V_FLUX_CLOB").toString();
//						} catch (Exception e) {
//							applicationException.fireTrace(e, "Error callback prGetFluxClobSByIdeven : FLUX_CLOB null , id event: {" + idEvenementReponse
//									+ "} erreur : {" + e.getMessage() + "}", "ListenerQueueCamGsimt", "loadParams");
//							logger.error(
//									"Error callback prGetFluxClobSByIdeven : FLUX_CLOB null : {} erreur : {}, message: {}, id: {}, tag: {}",
//									idEvenementReponse, e.getMessage(), null, null, null);
//							return;
//						}
//						logger.info("line : {}", 4);
//						logger.info("tag Out : {}", tagOut);
//						logger.info("flux Clob Out : {}", fluxClobOut);	
//						logger.info("_________________________ Send response (End)   _________________________");
//					}
					
					LocalDateTime then = LocalDateTime.now();
					logger.info("{}:{} - Recieve Message from :{} - Id message :{} - Time execution :{}", now, then,
							evt.getChannelName(), evt.getEventID(), now.until(then, ChronoUnit.SECONDS));
				}
				/*	}
		});
		executorService.shutdown();*/
	}
}
