package com.apiweather.app.jobs;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.apiweather.app.jobs.domain.SpacFile;



/**
 * @author a.bouabidi
 *
 */
@Configuration
@EnableBatchProcessing
public class IndarAppsBatchConfig {

	@Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;       	   
        
    @Bean
	public JobExecutionListener listener() {
		return new JobCompletionListener();
	}
    
    @Bean
    @Qualifier(value = "dssFileJob")
	public Job dssFileJob() {
		return jobBuilderFactory.get("dssFileJob")
				.incrementer(new RunIdIncrementer())
				.listener(listener())
				.flow(weatherDataStep())
				.end()
				.build();
	}
    
    @Bean
    @Qualifier(value = "spacLoadingJob")
   	public Job spacLoadingJob() {
   		return jobBuilderFactory.get("spacLoadingJob")
   				.incrementer(new RunIdIncrementer())
   				.listener(listener())
   				.flow(weatherDataStep())
   				.end()
   				.build();
   	}

    
	@Bean
    public Step weatherDataStep() {		
		WeatherDataStepRWP p = new WeatherDataStepRWP();
        return stepBuilderFactory.get("weatherData")
                .<SpacFile, SpacFile> chunk(10)
                .reader(p.weatherDataStepReader())
                //.processor(processor())
                .writer(p.weatherDataStepWriter())
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
