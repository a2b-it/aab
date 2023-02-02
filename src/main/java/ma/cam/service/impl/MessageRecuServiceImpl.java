package ma.cam.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import ma.cam.commun.Constants;
import ma.cam.dao.AbstractDAO;
import ma.cam.dto.MessageCsm;
import ma.cam.model.MessageOracle;
import ma.cam.model.ParamPs;
import ma.cam.service.MessageRecuService;
import ma.cam.xmlhandler.ListAllElementOfDOMElement;

@Service(value = "messageRecuService")
public class MessageRecuServiceImpl extends AbstractDAO implements MessageRecuService {
	static final String PK_VI_NAME = "PK_SIA_VINST_XML";
	static final String PK_VI_NAME_SIMULATION = "PK_GSIMT_VIR_INSTANTANES";
	
	
	Logger logger = LoggerFactory.getLogger(MessageRecuServiceImpl.class);

	@Override
	public MessageOracle prSetMessageRecu(String Nomprocedure, String idmessage, String tagrecu, String messagerecu, Object objRecu) throws Exception {
		Map<String, Object> map;
		ListAllElementOfDOMElement t = new ListAllElementOfDOMElement();
		final List<ParamPs> params = new ArrayList<>();
		int i = 1;

		map = t.getMapXmlKeyValue(messagerecu);

		/*for (Map.Entry<String, Object> entry : map.entrySet()) {
			logger.info("{}:{}", entry.getKey(), entry.getValue());
		}*/

		params.add(new ParamPs("V_IDMESSAGE", Constants.IN, i++, Constants.STRING, idmessage));
		params.add(new ParamPs("V_TAGRECU", Constants.IN, i++, Constants.STRING, tagrecu));
		params.add(new ParamPs("V_MESSAGERECU", Constants.IN, i++, Constants.STRING, messagerecu));
		params.add(new ParamPs("V_ELEMENTRECU", Constants.IN, i++, "T_VINST_ELEMENT", map));
		params.add(new ParamPs("V_IDEVENEMENTREPONSE", Constants.OUT, i++, Constants.STRING, null));
		params.add(new ParamPs("V_FLAGREPONDRECSM", Constants.OUT, i++, Constants.STRING, null));

		return executeProcedure(PK_VI_NAME, Nomprocedure, params);
	}

	@Override
	public MessageOracle prGetUnFluxClobSortant() throws Exception {
		final List<ParamPs> params = new ArrayList<>();
		int i = 1;
		params.add(new ParamPs("V_IDEVENEMENT", Constants.OUT, i++, Constants.STRING, null));
		params.add(new ParamPs("V_TAG", Constants.OUT, i++, Constants.STRING, null));
		params.add(new ParamPs("V_FLUX_CLOB", Constants.OUT, i++, Constants.STRING, null));
		params.add(new ParamPs("V_FLAGEXISTS", Constants.INOUT, i++, Constants.STRING, null));
		params.add(new ParamPs("V_TYPEMESSAGE", Constants.OUT, i++, Constants.STRING, null));
		params.add(new ParamPs("V_MESSAGE", Constants.OUT, i, Constants.STRING, null));

		return executeProcedure(PK_VI_NAME, "PR_GET_UN_FLUX_CLOB_SORTANT", params);
	}

	// V_ETATENVOICSM VA/ER
	@Override
	public MessageOracle prSetEtatEnvoiCsm(String idevenement,String statut,String error,String checksum) throws Exception {
		final List<ParamPs> params = new ArrayList<>();
		int i = 1;
		params.add(new ParamPs("V_IDEVENEMENT", Constants.IN, i++, Constants.STRING, idevenement));
		params.add(new ParamPs("V_STATUTENVOI", Constants.IN, i++, Constants.STRING, statut));
		params.add(new ParamPs("V_GRPHDR_CREDTTM", Constants.IN, i++, Constants.STRING, null));
		params.add(new ParamPs("V_ERREURENVOI", Constants.IN, i++, Constants.STRING, error));
		params.add(new ParamPs("V_CHECKSUM", Constants.IN, i++, Constants.STRING, checksum));
		
		params.add(new ParamPs("V_TYPEMESSAGE", Constants.IN, i++, Constants.STRING, null));
		params.add(new ParamPs("V_MESSAGE", Constants.IN, i, Constants.STRING, null));

		return executeProcedure(PK_VI_NAME, "PR_SET_FLUX_CLOB_S_BY_IDEVEN", params);
	}

	@Override
	public MessageOracle prCreerLog() throws Exception {
		final List<ParamPs> params = new ArrayList<>();
		int i = 1;
		params.add(new ParamPs("V_METHODE", Constants.IN, i++, Constants.STRING, null));
		params.add(new ParamPs("V_LOG", Constants.IN, i, Constants.STRING, null));

		return executeProcedure(PK_VI_NAME, "PR_CREER_LOG", params);
	}

	@Override
	public MessageOracle prGetFluxClobSByIdeven(String idEvent) throws Exception {
		final List<ParamPs> params = new ArrayList<>();
		int i = 1;
		params.add(new ParamPs("V_IDEVENEMENT", Constants.IN, i++, Constants.STRING, idEvent));
		params.add(new ParamPs("V_TAG_OUT", Constants.OUT, i++, Constants.STRING, null));
		params.add(new ParamPs("V_FLUX_CLOB", Constants.OUT, i++, Constants.STRING, null));
		params.add(new ParamPs("V_TYPEMESSAGE", Constants.OUT, i++, Constants.STRING, null));
		params.add(new ParamPs("V_MESSAGE", Constants.OUT, i, Constants.STRING, null));

		return executeProcedure(PK_VI_NAME, "PR_GET_FLUX_CLOB_S_BY_IDEVEN", params);
	}

	@Override
	public MessageOracle prSimulationGetUnFluxClobSortant() throws Exception {
		final List<ParamPs> params = new ArrayList<>();
		int i = 1;
		params.add(new ParamPs("V_IDEVENEMENT", Constants.OUT, i++, Constants.STRING, null));
		params.add(new ParamPs("V_URL", Constants.OUT, i++, Constants.STRING, null));
		params.add(new ParamPs("V_PORT", Constants.OUT, i++, Constants.STRING, null));
		params.add(new ParamPs("V_TAG", Constants.OUT, i++, Constants.STRING, null));
		params.add(new ParamPs("V_FLUX_CLOB", Constants.OUT, i++, Constants.STRING, null));
		params.add(new ParamPs("V_FLAGEXISTS", Constants.OUT, i++, Constants.STRING, null));
		params.add(new ParamPs("V_IDVIREMENT", Constants.OUT, i++, Constants.STRING, null));
		params.add(new ParamPs("V_QUEUE", Constants.OUT, i++, Constants.STRING, null));
		params.add(new ParamPs("V_TYPEMESSAGE", Constants.OUT, i++, Constants.STRING, null));
		params.add(new ParamPs("V_MESSAGE", Constants.OUT, i, Constants.STRING, null));

		return executeProcedure(PK_VI_NAME_SIMULATION, "PR_GET_UN_FLUX_CLOB_SORTANT", params);
	}
	
	@Override
	public MessageOracle prGetInfoReceive () throws Exception {
		final List<ParamPs> params = new ArrayList<>();
		int i = 1;
		params.add(new ParamPs("V_URL", Constants.OUT, i++, Constants.STRING, null));
		params.add(new ParamPs("V_PORT", Constants.OUT, i++, Constants.STRING, null));
		params.add(new ParamPs("V_QUEUE", Constants.OUT, i++, Constants.STRING, null));
		params.add(new ParamPs("V_NOMPROCEDURE", Constants.OUT, i++, Constants.STRING, null)); 
		params.add(new ParamPs("V_TYPEMESSAGE", Constants.OUT, i++, Constants.STRING, null));
		params.add(new ParamPs("V_MESSAGE", Constants.OUT, i, Constants.STRING, null));

		return executeProcedure(PK_VI_NAME_SIMULATION, "PR_GET_INFO_RECEIVE", params);
	}
	
	@Override
	public List<MessageCsm> prGetListeFluxClobSortant() throws Exception {
		final List<ParamPs> params = new ArrayList<ParamPs>();
		int i = 1;		
		params.add(new ParamPs(Constants.CURSOR_NAME, Constants.OUT,i++, Constants.CURSOR, null));
		//params.add(new ParamPs("V_FLAGEXISTS", Constants.OUT,i++, Constants.STRING, null));
		params.add(new ParamPs("V_TYPEMESSAGE", Constants.OUT,i++, Constants.STRING, null));	
		params.add(new ParamPs("V_MESSAGE", Constants.OUT,i++, Constants.STRING, null));	
		List<MessageCsm> listQueues = (List<MessageCsm>)       executeQueryProcedure(PK_VI_NAME, "PR_GET_LISTE_FLUX_CLOB_SORTANT", MessageCsm.class, params).getList();
		return listQueues;
	}
	
}
