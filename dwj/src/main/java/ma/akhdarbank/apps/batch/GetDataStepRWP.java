package ma.akhdarbank.apps.batch;

import java.util.List;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import ma.akhdarbank.apps.clients.ApiBatchMatchingClient;
import ma.akhdarbank.apps.dao.BatchRepository;
import ma.akhdarbank.apps.dao.TierRepositoryCustom;
import ma.akhdarbank.apps.model.TierBatch;
import ma.akhdarbank.apps.model.TierBatch.TierBatchRep;


@Component
public class GetDataStepRWP {
	
	private final String NUM_TICKET="NUM_TICKET";
	
	private final String AUTH_TOKEN="auth_token";
	
	private final String BATCH_SIZE="BATCH_SIZE";
	
	@Autowired
	private ApiBatchMatchingClient apiBatchMatchingClientImp;	
	
	@Autowired
	private BatchRepository batchRepo;
	
	@Autowired
	private TierRepositoryCustom tierRepository;
	
	
	
	@Bean
    public ItemReader<TierBatchRep> getDataReader() {
		// from rest
		return new RESTTierBatchResponseReader (batchRepo, apiBatchMatchingClientImp);
 
    }
	
	@Bean
    public ItemWriter<TierBatchRep> getDatawriter() {
		// to database
		return new GetDataStepWriter();
 
    }
	
	@Bean
    public ItemProcessor<TierBatch,Long> getDataProcessor() {
		// 
		return null;
 
    }
	
	@Bean
    public JobExecutionDecider getStepDecider() {
		// 
		return new GetDataJobExecutionDecider() ;
 
    }
	
	public class GetDataJobExecutionDecider implements JobExecutionDecider{

		@Override
		public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
			int nbrBatchRest = 0;
			if (stepExecution.getJobExecution().getExecutionContext().containsKey(BATCH_SIZE))
				nbrBatchRest = stepExecution.getJobExecution().getExecutionContext().getInt(BATCH_SIZE);
			return (nbrBatchRest==0)? new FlowExecutionStatus("COMPLETED"):new FlowExecutionStatus("CONTINUE");
		}
		
	
		
	}
   	 
	
	public class GetDataStepWriter implements ItemWriter<TierBatchRep> {
		
		private StepExecution stepExecution;
		
		

		public GetDataStepWriter() {			
			super();			
		}

		@Override
		public void write(List<? extends TierBatchRep> items) throws Exception {
			//
			//String auth_token = this.stepExecution.getJobExecution().getExecutionContext().getString(AUTH_TOKEN);
			//
			String num_Ticket = this.stepExecution.getJobExecution().getExecutionContext().getString(NUM_TICKET);
			
			// TODO Auto-generated method stub
			tierRepository.updateTiersBatchRep(items, num_Ticket);
		}
		
		@BeforeStep
		public void saveStepExecution(StepExecution stepExecution) {
			this.stepExecution = stepExecution;
		}
		
	}
}
