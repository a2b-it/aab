package ma.akhdarbank.apps;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ma.akhdarbank.apps.batch.AuthStepRWP;
import ma.akhdarbank.apps.batch.GetDataStepRWP;
import ma.akhdarbank.apps.batch.JobCompletionListener;
import ma.akhdarbank.apps.batch.PrepareDataStepRWP;
import ma.akhdarbank.apps.model.TierBatch.TierBatchRep;
import ma.akhdarbank.apps.model.Tiers;

@Configuration
public class DwjAppsBatchConfig {

	@Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;       
	    
    @Autowired
    private PrepareDataStepRWP prepareDataStepRWP;

    @Autowired
    private AuthStepRWP authStepRWP;

    @Autowired
    private GetDataStepRWP getDataStepRWP;

	@Bean
	public JobExecutionListener listener() {
		return new JobCompletionListener();
	}
    
    
    @Bean(name = "getDataJob")
	public Job getDataJob() throws Exception {
		return jobBuilderFactory.get("getDataJob")
				.incrementer(new RunIdIncrementer())
				.listener(listener())
				.start (authStep())	
				.next(getDataStep())
				.next(decider())
				 .on("COMPLETED").end()
		          .on("CONTINUE").to(getDataStep())
				.end()
				.build();
	}
    
    @Bean(name = "prepareDataJob")
	public Job prepareDataJob() throws Exception {
		return jobBuilderFactory.get("prepareDataJob")
				.incrementer(new RunIdIncrementer())
				.listener(listener())			
				.flow (authStep())
				.next(prepareDataStep ())
				.end()
				.build();
	}

    
    
	@Bean
    public Step authStep() {
		
        return stepBuilderFactory.get("auth")
                .<String, String> chunk(1)
                .reader(authStepRWP.authReader())
                //.processor(processor())
                .writer(authStepRWP.authWriter())                
                .build();
    }
	
	@Bean
    public Step prepareDataStep() throws Exception {
				
        return stepBuilderFactory.get("prepareData")
                .<Tiers, Tiers> chunk(1000)                
                .reader(prepareDataStepRWP.prepareDataReader())
                //.processor(processor())
                .writer(prepareDataStepRWP.prepareDataStepWriter())                
                .build();
    }

	@Bean
    public Step getDataStep() throws Exception {
				
        return stepBuilderFactory.get("getData")
                .<TierBatchRep, TierBatchRep> chunk(1000)                
                .reader(getDataStepRWP.getDataReader())
                //.processor(processor())
                .writer(getDataStepRWP.getDatawriter())                
                .build();
    }
	
	  
	  public JobExecutionDecider decider() {
	    return getDataStepRWP.getStepDecider();
	  }
	
	/*
	@Bean
    public Step preparDataMatchingStep() {
        return stepBuilderFactory.get("prapareData")
                .<String, String> chunk(10)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }
	
	@Bean
    public Step getDataMatchingResulatsStep() {
        return stepBuilderFactory.get("getMatchedData")
                .<String, String> chunk(10)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }
	
	@Bean
	public ExecutionContextPromotionListener promotionListener() {
		ExecutionContextPromotionListener listener = new ExecutionContextPromotionListener();
		listener.setKeys(new String[] {"auth_key"});

		return listener;
	}*/
	
	
}
