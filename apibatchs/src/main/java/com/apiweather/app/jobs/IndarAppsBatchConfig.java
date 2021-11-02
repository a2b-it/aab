package com.apiweather.app.jobs;

import java.io.IOException;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import com.apiweather.app.dss.model.DSSBlock;
import com.apiweather.app.dss.read.DSSFileReaderImp;
import com.apiweather.app.jobs.domain.SpacFile;
import com.apiweather.app.jobs.domain.WeatherPrecip;



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
    
    @Autowired
    private WeatherDataStepRWP weatherDataStepRWP;
    
    @Autowired
    private DssFileStepRWP dssFileStepRWP;
    
    @Autowired
    private DSSFileReaderImp dSSFileReaderImp;
    
    private static final String OVERRIDDEN_BY_EXPRESSION = null;
        
    @Bean
	public JobExecutionListener listener() {
		return new JobCompletionListener();
	}
    
    @Bean(name = "dssFileJob")	
	public Job dssFileJob() {
		return jobBuilderFactory.get("dssFileJob")
				.incrementer(new RunIdIncrementer())
				.listener(listener())
				.flow(weatherDataStep())
				.end()
				.build();
	}
    
    @Bean(name = "dssReadFileJob")	    
	public Job dssReadFileJob() throws IOException {
		return jobBuilderFactory.get("dssReadFileJob")
				.incrementer(new RunIdIncrementer())
				.listener(listener())
				.flow(dssReadFileDataStep())
				.end()
				.build();
	}
    
    /*
    @Bean
    @Qualifier(value = "spacLoadingJob")
   	public Job spacLoadingJob() {
   		return jobBuilderFactory.get("spacLoadingJob")
   				.incrementer(new RunIdIncrementer())
   				.listener(listener())
   				.flow(weatherDataStep())
   				.end()
   				.build();
   	}*/

    @Bean    
    public Step dssReadFileDataStep () throws IOException {		
    	
		//chunk is 1 block by block
        return stepBuilderFactory.get("dssReadFileDataStep")
                .<DSSBlock, DSSBlock> chunk(500)
                .reader(dssFileStepRWP.dssFileDataStepReader(dSSFileReaderImp))                
                .writer(dssFileStepRWP.dssApiDataStepWriter())                    
                .build();
    }
    
	@Bean	
    public Step weatherDataStep () {		
		WeatherDataStepRWP p = new WeatherDataStepRWP();
		//chunk is 1 block by block
        return stepBuilderFactory.get("weatherData")
                .<DSSBlock, DSSBlock> chunk(1)
                .reader(weatherDataStepRWP.weatherDataStepReader())                
                .writer(weatherDataStepRWP.weatherDataStepWriter())
                .stream((ItemStream) p.weatherDataStepReader())
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
