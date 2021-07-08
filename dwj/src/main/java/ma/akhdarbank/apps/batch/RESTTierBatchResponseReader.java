package ma.akhdarbank.apps.batch;

import java.util.Arrays;
import java.util.List;

import org.springframework.batch.core.StepExecution;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemStream;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import ma.akhdarbank.apps.model.TierBatch;

public class RESTTierBatchResponseReader implements ItemReader<TierBatch>, ItemStream {
	
	private static final String CURRENT_INDEX = "current.index";
	private final String apiUrl=null;
    private final RestTemplate restTemplate=null;
 
    private int nextTierIndex;
    
    private List<TierBatch> tierBatchData;
    
    private StepExecution stepExecution;
    

	@Override
	public TierBatch read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		if (tierBatchDataIsNotInitialized()) {
			tierBatchData = fetchTierBatchDataFromAPI();
        }
 
		TierBatch nextTierBatch = null;
 
        if (nextTierIndex < tierBatchData.size()) {
        	nextTierBatch = tierBatchData.get(nextTierIndex);
        	nextTierIndex++;
        }
        else {
        	nextTierIndex = 0;
        	tierBatchData = null;
        }
 
        return nextTierBatch;
	}
	
	private boolean tierBatchDataIsNotInitialized() {
        return this.tierBatchData == null;
    }
 
    private List<TierBatch> fetchTierBatchDataFromAPI() {
        ResponseEntity<TierBatch[]> response = restTemplate.getForEntity(apiUrl,
        		TierBatch[].class
        );
        TierBatch[] tierBatchData = response.getBody();
        return Arrays.asList(tierBatchData);
    }

	@Override
	public void open(ExecutionContext executionContext) throws ItemStreamException {
		if (executionContext.containsKey(CURRENT_INDEX)) {
			nextTierIndex = Long.valueOf(executionContext.getLong(CURRENT_INDEX)).intValue();
        }
        else {
        	nextTierIndex = 0;
        }
		
	}

	@Override
	public void update(ExecutionContext executionContext) throws ItemStreamException {
		executionContext.putLong(CURRENT_INDEX, Long.valueOf(nextTierIndex).longValue());
		
	}

	@Override
	public void close() throws ItemStreamException {}

}
