package ma.akhdarbank.apps;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import ma.akhdarbank.apps.batch.AuthStepRWP;
import ma.akhdarbank.apps.batch.ExecutionContextPromotionListener;
import ma.akhdarbank.apps.batch.JobCompletionListener;
import ma.akhdarbank.apps.batch.RESTTierBatchResponseReader;
import ma.akhdarbank.apps.clients.ApiAuthClient;
import ma.akhdarbank.apps.model.TierBatch;

@Configuration
@EnableBatchProcessing
public class DwjAppsBatchConfig {

	@Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;       
	
    @Autowired
    private ApiAuthClient apiAuthClientImp;
    
    
    @Bean
	public JobExecutionListener listener() {
		return new JobCompletionListener();
	}
    
    @Bean
	public Job processJob() {
		return jobBuilderFactory.get("processJob")
				.incrementer(new RunIdIncrementer())
				.listener(listener())
				.flow(orderStep1())
				.end()
				.build();
	}

    @Bean
    public Step step1(JdbcBatchItemWriter writer) {
        return stepBuilderFactory.get("step1")
          .<Coffee, Coffee> chunk(10)
          .reader(reader())
          .processor(processor())
          .writer(writer)
          .build();
    }
    
	@Bean
    public Step authStep() {
		AuthStepRWP authStepRWP = new AuthStepRWP();
        return stepBuilderFactory.get("auth")
                .<String, String> chunk(1)
                .reader(authStepRWP.new AuthStepReader(apiAuthClientImp))
                //.processor(processor())
                .writer(authStepRWP.new AuthStepWriter())
                .build();
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
