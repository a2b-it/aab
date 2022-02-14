package ma.akhdarbank.apps.batch;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.codehaus.jettison.json.JSONObject;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemStream;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;

import ma.akhdarbank.apps.clients.ApiBatchMatchingClient;
import ma.akhdarbank.apps.dao.BatchRepository;
import ma.akhdarbank.apps.model.LabFMatchingBatch;
import ma.akhdarbank.apps.model.TierBatch;
import ma.akhdarbank.apps.model.TierBatch.TierBatchRep;

public class RESTTierBatchResponseReader implements ItemReader<TierBatchRep>, ItemStream {
	
	private static final String CURRENT_INDEX = "current.index";	
    
	private final String AUTH_TOKEN="auth_token";
	
	private final String NUM_TICKET="NUM_TICKET";
	
	private final String BATCH_SIZE="BATCH_SIZE";
	
	private ApiBatchMatchingClient apiBatchMatchingClient=null;
 
    private int nextTierIndex = 0;       
    
    private List<TierBatchRep> tierBatchData;
    
    private StepExecution stepExecution;
    
    private List<LabFMatchingBatch> listeBatch;
    
    private BatchRepository batchRepo;
    

	public RESTTierBatchResponseReader (BatchRepository batchRepo, ApiBatchMatchingClient apiBatchMatchingClient) {	
		super();
		this.batchRepo= batchRepo;		
		this.apiBatchMatchingClient=apiBatchMatchingClient;
	}

	@Override
	public TierBatchRep read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		if (listeBatch==null || listeBatch.isEmpty()) {
			return null;
		}
		if (tierBatchDataIsNotInitialized()) {
			tierBatchData = fetchTierBatchDataFromAPI();
        }
		//processing  empty response
		if (tierBatchData!=null && tierBatchData.isEmpty()) {
			TierBatchRep t = new TierBatchRep();
			t.msgerreur="NOMATCHING";
			return t;
		}
		TierBatchRep nextTierBatch = null;
 
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
        return (this.tierBatchData == null );
    }
 
    private List<TierBatchRep> fetchTierBatchDataFromAPI() throws JsonProcessingException {
    	String auth_token = this.stepExecution.getJobExecution().getExecutionContext().getString(AUTH_TOKEN); 
    	LabFMatchingBatch idBatch = ((LinkedList<LabFMatchingBatch>)listeBatch).removeFirst();
    	TierBatchRep[] tierBatchData = apiBatchMatchingClient.getDataAfterMathing(auth_token, idBatch.getNumTicket());
    	this.stepExecution.getJobExecution().getExecutionContext().putString(NUM_TICKET,idBatch.getNumTicket());
    	this.stepExecution.getJobExecution().getExecutionContext().putInt(BATCH_SIZE,listeBatch.size());
        return Arrays.asList(tierBatchData);
    }

	@Override
	public void open(ExecutionContext executionContext) throws ItemStreamException {
		if (executionContext.containsKey(CURRENT_INDEX)) {
			nextTierIndex = Long.valueOf(executionContext.getLong(CURRENT_INDEX)).intValue();
        }
        else {        	        	
        	this.listeBatch = new LinkedList<LabFMatchingBatch>(batchRepo.findAllActiveBatch());        	
        	//nextTierIndex = 0;
        }
		
	}

	@Override
	public void update(ExecutionContext executionContext) throws ItemStreamException {
		executionContext.putLong(CURRENT_INDEX, Long.valueOf(nextTierIndex).longValue());							
	}

	@Override
	public void close() throws ItemStreamException {}
	
	@BeforeStep
	public void saveStepExecution(StepExecution stepExecution) {
		this.stepExecution = stepExecution;
	}

}
