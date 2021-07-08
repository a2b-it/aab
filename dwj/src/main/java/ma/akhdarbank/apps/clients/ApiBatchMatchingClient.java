package ma.akhdarbank.apps.clients;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;

import ma.akhdarbank.apps.model.TierBatch;
import ma.akhdarbank.apps.model.TierDTO;

public interface ApiBatchMatchingClient {

	
	
	public Long prepareDataForMathing(String token, List<TierDTO> tiers);
	
	public TierBatch[] getDataAfterMathing(String token, Long tiket) throws JsonProcessingException ;
}
