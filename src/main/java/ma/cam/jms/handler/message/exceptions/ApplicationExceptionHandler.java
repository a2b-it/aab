package ma.cam.jms.handler.message.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ma.cam.service.impl.TraceApplication;

public class ApplicationExceptionHandler extends Exception {
	private static final long serialVersionUID = 1L;
	
	@Autowired
	private TraceApplication traceApplication;
	
	Logger logger = LoggerFactory.getLogger(ApplicationExceptionHandler.class);
	public ApplicationExceptionHandler(Throwable e,String message,String className,String methode) {
		super(e);
		logger.error("{}: {}",message,e.getMessage());
		traceApplication.tracerExceptionApplication(className,methode, message+" : "+e.getMessage());
	}
	
}