package ma.alakhdarbank.apps;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import ma.alakhdarbank.ccb.AuthStepRWP;
import ma.alakhdarbank.ccb.ReadCTRRWP;
import ma.alakhdarbank.ccb.SendDataStepRWP;
import ma.alakhdarbank.ccb.entity.Ctr;



@Configuration
@EnableScheduling
public class CCBAppsBatchConfig {

	@Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;
    
    @Autowired
    private SendDataStepRWP sendDataStepRWP;
    
    @Autowired
    private ReadCTRRWP readCTRRWP;
   
	
	
    @Bean
	public JobExecutionListener listener() {
		return new JobCompletionListener();
	}
    
    @Bean(name = "sendingJob")
	public Job sendingJob() {
		return jobBuilderFactory.get("sendingJob")
				.incrementer(new RunIdIncrementer())
				.listener(listener())
				.flow(readJsonStep ())
				//.next(readJsonStep ())
				.end()
				.build();
	}

    @Bean(name = "ctrJob")    
	public Job ctrJob() {
		return jobBuilderFactory.get("ctrJob")
				.incrementer(new RunIdIncrementer())
				.listener(listener())
				.flow(readCtrStep ())
				//.next(readCtrStep ())
				.end()
				.build();
	}
    
  
    
	@Bean
    public Step readJsonStep() {
		
        return stepBuilderFactory.get("readJsonStep")
                .<String, String> chunk(1)
                .reader(sendDataStepRWP.jsonFileReader())
                .processor(sendDataStepRWP.jsonFileProcessor())
                .writer(sendDataStepRWP.jsonFileWriter())
                .build();
    }

	
	@Bean
    public Step readCtrStep() {
		
        return stepBuilderFactory.get("readCtrStep")
                .<String, Ctr> chunk(1)
                .reader(readCTRRWP.ctrFileReader())
                .processor(readCTRRWP.jsonCtrProcessor())
                .writer(readCTRRWP.ctrFileWriter())
                .build();
    }
	
	
}
