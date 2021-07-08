package ma.akhdarbank.apps;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import ma.akhdarbank.apps.batch.ExecutionContextPromotionListener;
import ma.akhdarbank.apps.batch.RESTTierBatchResponseReader;
import ma.akhdarbank.apps.model.TierBatch;

@Configuration
@EnableBatchProcessing
public class DwjAppsBatchConfig {

	@Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;
    
	
	
	@Bean
    public Step authStep() {
        return stepBuilderFactory.get("auth")
                .<String, String> chunk(1)
                .reader(authReader())
                .processor(processor())
                .writer(writer())
                .build();
    }
	
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
	}
}
