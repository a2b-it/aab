package ma.akhdarbank.apps.clients;

import com.fasterxml.jackson.core.JsonProcessingException;

import ma.akhdarbank.apps.model.TierBatch;
import ma.akhdarbank.apps.model.TierBatchs;

public interface ApiBatchMatchingClient {

	
	
	public Long prepareDataForMathing(String token, TierBatchs tiers);
	
	public TierBatch[] getDataAfterMathing(String token, Long tiket) throws JsonProcessingException ;
}
