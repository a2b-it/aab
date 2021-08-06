package com.apiweather.app.tools;



import java.net.http.HttpHeaders;

import javax.batch.operations.JobRestartException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpClientErrorException.Unauthorized;
import org.springframework.web.context.request.WebRequest;




@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler       	 {
	
		private static final Logger logger = LoggerFactory.getLogger(RestExceptionHandler.class);	
		
		private ResponseEntity<Object> buildResponseEntity(HttpStatus status, Exception ex) {
			ApiError apiError =  new ApiError(status);
			apiError.setMessage(ex.getMessage());
			logger.error(ex.getMessage(), ex);
		    return new ResponseEntity<>(apiError, apiError.getStatus());
		}
		
	    @ExceptionHandler(value = { JobRestartException.class })	
	    public ResponseEntity<Object> handleJobRestartException(JobRestartException  ex) {	
	        logger.error("JobRestartException: ",ex.getMessage());		     	        
	        return buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR,ex);
	    }
	
	    
	
	   	
	    @ExceptionHandler(value = { JobExecutionAlreadyRunningException.class })	
	    public ResponseEntity<Object> handleJobExecutionAlreadyRunningException(JobExecutionAlreadyRunningException ex) {	
	    	logger.error("JobExecutionAlreadyRunningException : ",ex.getMessage());		       
	        return buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR,ex);
	    }
	    
	    @ExceptionHandler(value= {JobParametersInvalidException.class})
	    protected ResponseEntity<Object> handleJobParametersInvalidException(JobParametersInvalidException ex) {
	    	logger.error("JobParametersInvalidException Exception: ",ex.getMessage());	       
	        return buildResponseEntity(HttpStatus.NOT_FOUND,ex);
	    }    	    	   	   
	   
	   
	    @ExceptionHandler(JobInstanceAlreadyCompleteException.class)
	    ResponseEntity<Object> handleJobInstanceAlreadyCompleteException(JobInstanceAlreadyCompleteException e) {
	    	logger.error("ConstraintViolation Exception: ",e.getMessage());		    	 	    	
	      return buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR,e);
	    } 
	    
	    
	    @ExceptionHandler(value = { Unauthorized.class })	
	    public ResponseEntity<Object> handleUnauthorizedException(Unauthorized ex) {	
	    	logger.error("Unauthorized Exception: ",ex.getMessage());	
	    	 ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST);
		        apiError.setMessage(ex.getMessage());
	        return buildResponseEntity(HttpStatus.FORBIDDEN,ex);	
	    }
	    
	    	    	
	   
}
