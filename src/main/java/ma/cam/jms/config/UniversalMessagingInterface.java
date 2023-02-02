package ma.cam.jms.config;

import com.pcbsys.nirvana.client.nChannel;
import com.pcbsys.nirvana.client.nConsumeEvent;
import com.pcbsys.nirvana.client.nQueue;
import com.pcbsys.nirvana.client.nSession;

import ma.cam.dto.ParamsQueue;
import ma.cam.jms.handler.message.exceptions.ActionNotPossibleException;
import ma.cam.jms.handler.message.exceptions.CannotReadFromQueueException;
import ma.cam.jms.handler.message.exceptions.CannotSendToQueueException;
import ma.cam.jms.handler.message.exceptions.SessionCreationException;

public interface UniversalMessagingInterface {

	public nSession connectionFactory() throws SessionCreationException;
	public boolean disconnect(nSession session);
	
	public nQueue getQueue(String queueName) throws ActionNotPossibleException;
	public ParamsQueue getQueueParams(String queueName) throws ActionNotPossibleException;
	public nChannel getChannel(nSession session, String channelName) throws ActionNotPossibleException;
	
	public nConsumeEvent getMessagesFromQueue(nQueue queue) throws CannotReadFromQueueException;
	public nConsumeEvent sendMessagesToQueue(nQueue queue,String tag,String message,String messageId) throws CannotSendToQueueException;
}
