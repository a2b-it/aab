package com.apiweather.app.tools;



import java.net.http.HttpHeaders;

import javax.validation.ConstraintViolationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import com.apiweather.app.tools.exception.BusinessException;
import com.apiweather.app.tools.exception.EntityNotFoundException;


@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler       	 {
	
		private static final Logger logger = LoggerFactory.getLogger(RestExceptionHandler.class);	
		
		private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
		       return new ResponseEntity<>(apiError, apiError.getStatus());
		}
		
	    @ExceptionHandler(value = { IllegalArgumentException .class })	
	    public ResponseEntity<Object> handleInvalidInputException(IllegalArgumentException  ex) {	
	        logger.error("Invalid Input Exception: ",ex.getMessage());	
	        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST);
		    apiError.setMessage(ex.getMessage());
	        
	        return buildResponseEntity(apiError);
	    }
	
	    
	
	   	
	    @ExceptionHandler(value = { BusinessException.class })	
	    public ResponseEntity<Object> handleBusinessException(BusinessException ex) {	
	    	logger.error("Business Exception: ",ex.getMessage());	
	    	ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR);
		    apiError.setMessage(ex.getMessage());
	       
	        return buildResponseEntity(apiError);
	    }
	    
	    	    	    	   	   
	    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
	        HttpHeaders headers, HttpStatus status, WebRequest request) {
	    	logger.error("ConstraintViolation Exception: ",ex.getMessage());	
	    	ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST);
		     apiError.setMessage(ex.getMessage());
		     apiError.setDebugMessage("Validation Failed: ");
	    	 
	      
	          //ex.getBindingResult().toString());
	      return buildResponseEntity(apiError);
	    }  
	   
	    @ExceptionHandler(ConstraintViolationException.class)
	    @ResponseStatus(HttpStatus.BAD_REQUEST)
	    ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException e) {
	    	logger.error("ConstraintViolation Exception: ",e.getMessage());	
	    	 ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST);
		     apiError.setMessage(e.getMessage());
		     apiError.setDebugMessage("not valid due to validation error: ");
		     //apiError.getSubErrors().addAll(e.getConstraintViolations());
	    	
	      return buildResponseEntity(apiError);
	    } 
	    
	    
	    @ExceptionHandler(value = { Unauthorized.class })	
	    public ResponseEntity<Object> handleUnauthorizedException(Unauthorized ex) {	
	    	logger.error("Unauthorized Exception: ",ex.getMessage());	
	    	 ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST);
		        apiError.setMessage(ex.getMessage());
	        return buildResponseEntity(apiError);	
	    }
	    
	    @ExceptionHandler(value= {EntityNotFoundException.class})
	    protected ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException ex) {
	    	logger.error("EntityNotFound Exception: ",ex.getMessage());
	        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND);
	        apiError.setMessage(ex.getMessage());
	        return buildResponseEntity(apiError);
	    }
	    
	 
	   
}
