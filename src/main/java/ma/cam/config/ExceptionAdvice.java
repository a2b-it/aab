package ma.cam.config;

import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ExceptionAdvice extends ResponseEntityExceptionHandler{

//	@ResponseBody
//	@ExceptionHandler(RuntimeException.class)
//	  public ResponseEntity<Object> servletException(ServletException e, WebRequest request) {
//	    String message = e.getMessage();
//	    ApiResponse<String> restErrorResponse = new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), message,null);
//	    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(restErrorResponse);
//	  }
	
	
	
}
