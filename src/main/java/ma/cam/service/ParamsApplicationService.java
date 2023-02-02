package ma.cam.service;

import java.util.List;

import ma.cam.dto.ParamsApp;
import ma.cam.dto.ParamsQueue;

public interface ParamsApplicationService {

	public List<ParamsQueue> getListQueues()  throws Exception;
	
	public List<ParamsApp> getListParamsApp()  throws Exception;
	
}
