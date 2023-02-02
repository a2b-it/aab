package ma.cam.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pcbsys.nirvana.client.nChannel;
import com.pcbsys.nirvana.client.nChannelAttributes;
import com.pcbsys.nirvana.client.nChannelNotFoundException;
import com.pcbsys.nirvana.client.nConsumeEvent;
import com.pcbsys.nirvana.client.nEventAttributes;
import com.pcbsys.nirvana.client.nEventProperties;
import com.pcbsys.nirvana.client.nIllegalArgumentException;
import com.pcbsys.nirvana.client.nIllegalChannelMode;
import com.pcbsys.nirvana.client.nMaxBufferSizeExceededException;
import com.pcbsys.nirvana.client.nQueue;
import com.pcbsys.nirvana.client.nRequestTimedOutException;
import com.pcbsys.nirvana.client.nSecurityException;
import com.pcbsys.nirvana.client.nSession;
import com.pcbsys.nirvana.client.nSessionNotConnectedException;
import com.pcbsys.nirvana.client.nSessionPausedException;
import com.pcbsys.nirvana.client.nTransaction;
import com.pcbsys.nirvana.client.nTransactionAlreadyAbortedException;
import com.pcbsys.nirvana.client.nTransactionAlreadyCommittedException;
import com.pcbsys.nirvana.client.nTransactionAttributes;
import com.pcbsys.nirvana.client.nTransactionException;
import com.pcbsys.nirvana.client.nTransactionFactory;
import com.pcbsys.nirvana.client.nTransactionNoSuchTXIDException;
import com.pcbsys.nirvana.client.nTransactionNotStartedException;
import com.pcbsys.nirvana.client.nUnexpectedResponseException;
import com.pcbsys.nirvana.client.nUnknownRemoteRealmException;

import ma.cam.dto.ParamsQueue;
import ma.cam.jms.config.ConnectionFactoryNirvana;
import ma.cam.jms.config.UniversalMessagingInterface;
import ma.cam.jms.handler.message.exceptions.ActionNotPossibleException;
import ma.cam.jms.handler.message.exceptions.CannotReadFromQueueException;
import ma.cam.jms.handler.message.exceptions.CannotSendToQueueException;
import ma.cam.jms.handler.message.exceptions.GlobalApplicationException;
import ma.cam.jms.handler.message.exceptions.SessionCreationException;
import ma.cam.tools.StringTools;

@Service
public class MessageHandlerImpl implements UniversalMessagingInterface{
	Logger logger = LoggerFactory.getLogger(MessageHandlerImpl.class);
	
	@Autowired
	private GlobalApplicationException	applicationException;
	
	@Override
	public nSession connectionFactory() throws SessionCreationException {
		if(!ConnectionFactoryNirvana.SESSIONFACTORYAPI.isConnected()) {
			applicationException.fireTrace (new Exception("You ar not connected!!"),"You ar not connected !!!!","MessageHandlerImpl", "loadParams");
			throw new SessionCreationException(new Exception("You ar not connected!!"),"You ar not connected!!");
		}
		return ConnectionFactoryNirvana.SESSIONFACTORYAPI.isConnected()?ConnectionFactoryNirvana.SESSIONFACTORYAPI:null ;
	}

	@Override
	public boolean disconnect(nSession session) {
		return false;
	}

	
	@Override
	public nQueue getQueue(String queueName) throws ActionNotPossibleException {
		nChannelAttributes cattrib = new nChannelAttributes();
		try {
			cattrib.setName(queueName);
		} catch (nIllegalArgumentException e) {
			e.printStackTrace();
		}
        try {
			return connectionFactory().findQueue(cattrib);
		} catch (nChannelNotFoundException e) {
			applicationException.fireTrace (e,"You ar not connected !!!!","MessageHandlerImpl", "loadParams");
			throw new ActionNotPossibleException(new Exception("You ar not connected <nChannelNotFoundException>!!"));
		} catch (nSessionPausedException e) {
			applicationException.fireTrace (e,"You ar not connected !!!!","MessageHandlerImpl", "loadParams");
			throw new ActionNotPossibleException(new Exception("You ar not connected <nSessionPausedException>!!"));
		} catch (nUnknownRemoteRealmException e) {
			applicationException.fireTrace (e,"You ar not connected !!!!","MessageHandlerImpl", "loadParams");
			throw new ActionNotPossibleException(new Exception("You ar not connected <nUnknownRemoteRealmException>!!"));
		} catch (nSecurityException e) {
			applicationException.fireTrace (e,"You ar not connected !!!!","MessageHandlerImpl", "loadParams");
			throw new ActionNotPossibleException(new Exception("You ar not connected <nSecurityException>!!"));
		} catch (nSessionNotConnectedException e) {
			applicationException.fireTrace (e,"You ar not connected !!!!","MessageHandlerImpl", "loadParams");
			throw new ActionNotPossibleException(new Exception("You ar not connected <nSessionNotConnectedException>!!"));
		} catch (nIllegalArgumentException e) {
			applicationException.fireTrace (e,"You ar not connected !!!!","MessageHandlerImpl", "loadParams");
			throw new ActionNotPossibleException(new Exception("You ar not connected <nIllegalArgumentException>!!"));
		} catch (nUnexpectedResponseException e) {
			applicationException.fireTrace (e,"You ar not connected !!!!","MessageHandlerImpl", "loadParams");
			throw new ActionNotPossibleException(new Exception("You ar not connected <nUnexpectedResponseException>!!"));
		} catch (nRequestTimedOutException e) {
			applicationException.fireTrace (e,"You ar not connected !!!!","MessageHandlerImpl", "loadParams");
			throw new ActionNotPossibleException(new Exception("You ar not connected <nRequestTimedOutException>!!"));
		} catch (nIllegalChannelMode e) {
			applicationException.fireTrace (e,"You ar not connected !!!!","MessageHandlerImpl", "loadParams");
			throw new ActionNotPossibleException(new Exception("You ar not connected <nIllegalChannelMode>!!"));
		} catch (SessionCreationException e) {
			applicationException.fireTrace (e,"You ar not connected !!!!","MessageHandlerImpl", "loadParams");
			throw new ActionNotPossibleException(new Exception("You ar not connected <SessionCreationException>!!"));
		}
	}

	@Override
	public nChannel getChannel(nSession session, String channelName) throws ActionNotPossibleException {
		return null;
	}

	@Override
	public nConsumeEvent getMessagesFromQueue(nQueue queue) throws CannotReadFromQueueException {
		return null;
	}

	@Override
	public nConsumeEvent sendMessagesToQueue(nQueue queue,String tag,String message,String messageId) throws CannotSendToQueueException {
		logger.info("--Sending Message <{}> tag :{}" ,new Date(),tag);
		
		//Transaction Attributes
		nTransactionAttributes tattrib;
		try {
			tattrib = new nTransactionAttributes(queue);
		} catch (nTransactionException e) {
			throw new CannotSendToQueueException(e,"Error sending message (nTransactionAttributes)");
		}
        nTransaction myTransaction = nTransactionFactory.create(tattrib);
        //-------------------------------------------
        
        nConsumeEvent evt = new nConsumeEvent(tag, message.getBytes());
        
        nEventAttributes evtAttrs = new nEventAttributes();
        evtAttrs.setMessageType((byte) 5);
        
        nEventProperties evtProperties = new nEventProperties();
        
        evtProperties.put("messageID", messageId);
        evtProperties.put("messageName", tag);
        
        //date 
        Long ums = evt.getTimestamp();
        Date date = new Date(ums);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy'-'MM'-'dd'T'HH':'mm':'ss'.'SSS");
        String localTimestamp = sdf.format(date);        
        evtProperties.put("depositTimestamp", localTimestamp);
        //-----------------------------------------------
        
        //checksum : Résultat d’un algorithme SHA 256 appliqué au message fonctionnel, utilisé pour contrôler que le message n’a pas été altéré ou falsifié
        //String sha = checksum.code(message, false); //-- code livrer par GSIMT
        String sha = StringTools.getSHA256Hash(message);
        
        evtProperties.put("checksum", sha);
        //---------------------------------------------
        
        evt.setAttributes(evtAttrs);
        evt.setProperties(evtProperties);
        
        logger.info(" tag : {} , id Message : {} " ,tag,messageId);
        
        try {
			myTransaction.publish(evt);
		} catch (nTransactionAlreadyCommittedException | nTransactionAlreadyAbortedException |
				nSessionNotConnectedException | nSessionPausedException  | nSecurityException | nMaxBufferSizeExceededException e) {
			throw new CannotSendToQueueException(e,"Error sending message (publish) ");
		} 
        
        
        try {
			myTransaction.commit();
		} catch (nTransactionNoSuchTXIDException | nTransactionAlreadyCommittedException | nTransactionAlreadyAbortedException |
				nSessionNotConnectedException | nRequestTimedOutException | nUnexpectedResponseException |
				nSessionPausedException | nTransactionNotStartedException | nChannelNotFoundException | nSecurityException | nMaxBufferSizeExceededException 
					e) {
			throw new CannotSendToQueueException(e,"");
		} 
        logger.info("--Sending Message <{}> tag :{}" ,new Date(),tag);
        return evt;
	}

	@Override
	public ParamsQueue getQueueParams(String queueName) throws ActionNotPossibleException {
		ParamsQueue paramQueue = ConnectionFactoryNirvana.LISTQUEUE.stream()
				  .filter(p -> queueName.equalsIgnoreCase(p.getCodequeuescpi()))
				  .findAny()
				  .orElse(null);
		return paramQueue;
	}
	
	
	
	
	
}
