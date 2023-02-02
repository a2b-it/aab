package ma.cam.service.impl;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import ma.cam.commun.Constants;
import ma.cam.dao.AbstractDAO;
import ma.cam.dto.ParamsApp;
import ma.cam.dto.ParamsQueue;
import ma.cam.model.ParamPs;
import ma.cam.service.ParamsApplicationService;

@Service(value = "ParamsApplicationService")
public class ParamsApplicationImpl extends AbstractDAO implements ParamsApplicationService {
	static final String PK_VI_NAME = "PK_SIA_VINST_XML";

	@Override
	public List<ParamsQueue> getListQueues() throws Exception {
		final List<ParamPs> params = new ArrayList<ParamPs>();
		int i = 1;		
		params.add(new ParamPs(Constants.CURSOR_NAME, Constants.OUT,i++, Constants.CURSOR, null));
		params.add(new ParamPs("TYPEMESSAGE", Constants.OUT,i++, Constants.STRING, null));	
		params.add(new ParamPs("MESSAGE", Constants.OUT,i++, Constants.STRING, null));	
		List<ParamsQueue> listQueues = (List<ParamsQueue>)       executeQueryProcedure(PK_VI_NAME, "PR_GET_LIST_QUEUE", ParamsQueue.class, params).getList();
		return listQueues;
	}
	
	@Override
	public List<ParamsApp> getListParamsApp() throws Exception {
		final List<ParamPs> params = new ArrayList<ParamPs>();
		int i = 1;		
		params.add(new ParamPs(Constants.CURSOR_NAME, Constants.OUT,i++, Constants.CURSOR, null));
		params.add(new ParamPs("TYPEMESSAGE", Constants.OUT,i++, Constants.STRING, null));	
		params.add(new ParamPs("MESSAGE", Constants.OUT,i++, Constants.STRING, null));
		return (List<ParamsApp>)       executeQueryProcedure(PK_VI_NAME, "PR_GET_PARAMETERS", ParamsApp.class, params).getList();
		}

}
