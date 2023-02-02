package ma.cam.jms.handler.message.exceptions;

import com.pcbsys.nirvana.client.nTransactionAlreadyCommittedException;

public class CannotSendToQueueException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public CannotSendToQueueException(Throwable e,String message) {
		super(e);
		if (e instanceof nTransactionAlreadyCommittedException) {
			
		}
		
	}

}
