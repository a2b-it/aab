package ma.akhdarbank.apps.batch;

import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import ma.akhdarbank.apps.clients.ApiBatchMatchingClient;
import ma.akhdarbank.apps.dao.TierRepositoryCustom;
import ma.akhdarbank.apps.model.TierBatch;
import ma.akhdarbank.apps.model.TierBatchs;
import ma.akhdarbank.apps.model.Tiers;


@Component
public class PrepareDataStepRWP {
	
	private final String AUTH_TOKEN="auth_token";

	@Autowired
	private JdbcPagingItemReader<Tiers> reader;
	
	@Autowired
	private TierRepositoryCustom tierRepository;
	
	@Autowired
	private ApiBatchMatchingClient apiBatchMatchingClientImp;		
		
	@Autowired
	public PrepareDataStepRWP() {		
		super();						
	}
	
	


	public JdbcPagingItemReader<Tiers> prepareDataReader() throws Exception {
				
		return reader;
	}
	
	

	
	
	
	@Bean
	/**
	 * save data to remote api
	 * @return
	 */
    public ItemWriter<Tiers> prepareDataStepWriter() {		
		return new PrepareDataStepWriter(apiBatchMatchingClientImp);
 
    }

	public class PrepareDataStepWriter implements ItemWriter<Tiers> {

		private StepExecution stepExecution;
		
		private ApiBatchMatchingClient apiBatchMatchingClientImp;	
		
		private String mode="";
		
		private Integer seuil=0;
		
		private Integer limit=100;
		
		private String typeList="";
		
		

		public PrepareDataStepWriter(ApiBatchMatchingClient apiBatchMatchingClientImp) {
			super();
			this.apiBatchMatchingClientImp = apiBatchMatchingClientImp;
		}
		

		@Override
		public void write(List<? extends Tiers> items) throws Exception {
			String auth_token = this.stepExecution.getJobExecution().getExecutionContext().getString(AUTH_TOKEN);
			
			List<TierBatch.TierBatchReq> list = new ArrayList<TierBatch.TierBatchReq>();
			for (Tiers t : items) {
				list.add(t.toDTOObject());
			}
			TierBatchs tl = new TierBatchs ();
			tl.setClients(list);			
			Long ticket = this.apiBatchMatchingClientImp.prepareDataForMathing(auth_token, tl);
			tierRepository.updateTiersBatchReq(tl.getClients(), ticket);
		}

		@BeforeStep
		public void saveStepExecution(StepExecution stepExecution) {
			this.stepExecution = stepExecution;
		}

	}
	/*
	 * @BeforeStep public void retrieveInterstepData(StepExecution stepExecution) {
	 * JobExecution jobExecution = stepExecution.getJobExecution(); ExecutionContext
	 * jobContext = jobExecution.getExecutionContext(); this.someObject =
	 * jobContext.get("someKey"); }
	 */
}
