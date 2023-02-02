package ma.cam.service;

import ma.cam.model.MessageOracle;

public interface TraceService {

	public MessageOracle vinstApplicationTracer(String objet, String methode,String message)  throws Exception;
	
}
