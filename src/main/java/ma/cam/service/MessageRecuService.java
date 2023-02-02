package ma.cam.service;

import java.util.List;

import ma.cam.dto.MessageCsm;
import ma.cam.dto.ParamsQueue;
import ma.cam.model.MessageOracle;

public interface MessageRecuService {

	public MessageOracle prSetMessageRecu(String Nomprocedure, String idmessage,String tagrecu,String messagerecu,Object objRecu)  throws Exception;
	
	public MessageOracle prGetUnFluxClobSortant() throws Exception;
	
	public MessageOracle prGetFluxClobSByIdeven(String idEvent) throws Exception;
	
	public MessageOracle prSetEtatEnvoiCsm(String idevenement,String statut,String error,String checksum) throws Exception ;
	
	public MessageOracle prCreerLog() throws Exception ;
	
	List<MessageCsm> prGetListeFluxClobSortant() throws Exception;
	
	
	// simulattion get one message from database
	public MessageOracle prSimulationGetUnFluxClobSortant() throws Exception;

	public MessageOracle prGetInfoReceive() throws Exception;

	
	
}
