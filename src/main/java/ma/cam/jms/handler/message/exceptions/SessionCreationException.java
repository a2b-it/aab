package ma.cam.jms.handler.message.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SessionCreationException extends Exception {
	
	private static final long serialVersionUID = 1L;
	Logger logger = LoggerFactory.getLogger(SessionCreationException.class);
	public SessionCreationException(Throwable e,String message) {
		super(e);
		logger.error(message);
		e.printStackTrace();
		//System.exit(0);
		
	}
}
