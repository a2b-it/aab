package ma.cam.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ma.cam.service.TraceService;

@Service
public class TraceApplication {
	@Autowired
	private TraceService traceService;
	
	Logger logger = LoggerFactory.getLogger(TraceApplication.class);
	
	public void tracerExceptionApplication(String objet,String methode,String message) {
		
		//trace Database 
		
		/*try {
			traceService.vinstApplicationTracer(objet, methode, message);
		} catch (Exception e) {
			logger.error("Error call VINST_APPLICATION_TRACER Exception:{}",e.getMessage());
		}*/
	}


	
}
