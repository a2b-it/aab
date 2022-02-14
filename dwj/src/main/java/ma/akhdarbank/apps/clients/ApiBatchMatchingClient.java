package ma.akhdarbank.apps.clients;

import ma.akhdarbank.apps.excp.DWJCallException;
import ma.akhdarbank.apps.model.TierBatch.TierBatchRep;
import ma.akhdarbank.apps.model.TierBatchs;

public interface ApiBatchMatchingClient {

	
	
	public Long prepareDataForMathing(String token, TierBatchs tiers)  throws DWJCallException;
	
	public TierBatchRep[] getDataAfterMathing(String token, String tiket) ;
}
