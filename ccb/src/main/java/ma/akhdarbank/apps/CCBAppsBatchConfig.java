package ma.akhdarbank.apps;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ma.alakhdarbank.ccb.SendDataStepRWP;



@Configuration
public class CCBAppsBatchConfig {

	@Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;
    
    @Autowired
    private SendDataStepRWP sendDataStepRWP;
    
    
    @Bean
	public JobExecutionListener listener() {
		return new JobCompletionListener();
	}
    
    @Bean(name = "processJob")
	public Job processJob() {
		return jobBuilderFactory.get("processJob")
				.incrementer(new RunIdIncrementer())
				.listener(listener())
				.flow(readJsonStep ())
				.end()
				.build();
	}

    
    
	@Bean
    public Step readJsonStep() {
		
        return stepBuilderFactory.get("processStep")
                .<String, String> chunk(1)
                .reader(sendDataStepRWP.jsonFileReader())
                //.processor(processor())
                .writer(sendDataStepRWP.jsonFileWriter())
                .build();
    }

	
	
}
