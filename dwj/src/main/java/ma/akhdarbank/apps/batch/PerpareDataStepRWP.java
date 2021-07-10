package ma.akhdarbank.apps.batch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import ma.akhdarbank.apps.clients.ApiBatchMatchingClient;
import ma.akhdarbank.apps.dao.TierRepositoryCustom;
import ma.akhdarbank.apps.dao.TierRowMapper;
import ma.akhdarbank.apps.model.TierBatch;
import ma.akhdarbank.apps.model.TierBatchs;
import ma.akhdarbank.apps.model.Tiers;

public class PerpareDataStepRWP {
	
	
	private DataSource dataSource;
	
	@Autowired
	private TierRepositoryCustom tierRepository;
		
	@Autowired
	public PerpareDataStepRWP(DataSource dataSource) {
		super();
		this.dataSource = dataSource;
	}

	@Bean
	public JdbcPagingItemReader<Tiers> itemReader() throws Exception {
		//
		Map<String, Object> parameterValues = new HashMap<>();
		//parameterValues.put("status", "NEW");
		//
		PagingQueryProvider queryProvider= (PagingQueryProvider) queryProvider();
		
		return new JdbcPagingItemReaderBuilder<Tiers>()
   				.name("tiersReader")
   				.dataSource(this.dataSource)
   				.queryProvider(queryProvider)
   				.parameterValues(parameterValues)
   				.rowMapper(new TierRowMapper ())
   				.pageSize(1000)
   				.build();
	}
	
	
	public PagingQueryProvider queryProvider() throws Exception {
		SqlPagingQueryProviderFactoryBean provider = new SqlPagingQueryProviderFactoryBean();
		provider.setSelectClause("select IDENTIFIANT,TYPEPERSONNE,NOM,PRENOM,RAISONSOCIALE,ETX_RIM ");
		provider.setFromClause("from tiers");
		//provider.setWhereClause("where status=:status");
		provider.setSortKey("IDENTIFIANT");

		return provider.getObject();
	}

	public class PerpareDataStepWriter implements ItemWriter<Tiers> {

		private StepExecution stepExecution;
		
		private ApiBatchMatchingClient apiBatchMatchingClientImp;
		
		private String auth_token;		
		
		private String mode="";
		
		private Integer seuil=0;
		
		private Integer limit=100;
		
		private String typeList="";

		@Override
		public void write(List<? extends Tiers> items) throws Exception {
			ExecutionContext stepContext = this.stepExecution.getExecutionContext();
			this.auth_token = (String) stepContext.get("auth_token");
			List<TierBatch.TierBatchReq> list = new ArrayList<TierBatch.TierBatchReq>();
			for (Tiers t : items) {
				list.add(t.toDTOObject());
			}
			TierBatchs tl = new TierBatchs ();
			tl.setClients(list);			
			Long ticket = this.apiBatchMatchingClientImp.prepareDataForMathing(auth_token, tl);
			tierRepository.updateTiersBatch(tl.getClients(), ticket);
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
