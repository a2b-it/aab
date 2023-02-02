package ma.cam.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ma.cam.commun.Constants;
import ma.cam.dao.AbstractDAO;
import ma.cam.model.MessageOracle;
import ma.cam.model.ParamPs;
import ma.cam.service.TraceService;
import ma.cam.xmlhandler.ListAllElementOfDOMElement;

@Service
public class TraceServiceImpl extends AbstractDAO implements TraceService{
	static final String PK_VI_NAME = "PK_SIA_VINST_APPL_TOOLS";
	Logger logger = LoggerFactory.getLogger(TraceServiceImpl.class);

	@Override
	public MessageOracle vinstApplicationTracer(String objet, String methode, String message) throws Exception {
		final List<ParamPs> params = new ArrayList<>();
		int i = 1;

		params.add(new ParamPs("V_CLASS", Constants.IN, i++, Constants.STRING, objet));
		params.add(new ParamPs("V_METHODE", Constants.IN, i++, Constants.STRING, methode));
		params.add(new ParamPs("V_MESSAGE", Constants.IN, i++, Constants.STRING, message));

		return executeProcedure(PK_VI_NAME, "VINST_APPLICATION_TRACER", params);
	}
	
	
	
}
