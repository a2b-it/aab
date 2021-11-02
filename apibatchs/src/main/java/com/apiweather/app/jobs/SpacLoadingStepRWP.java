package com.apiweather.app.jobs;

import java.util.List;

import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.FileSystemResource;

import com.apiweather.app.jobs.domain.SpacFile;
import com.apiweather.app.jobs.domain.SpacFileMapper;
import com.apiweather.app.rest.client.IndarApiCallerImp;



/**
 * @author a.bouabidi
 *
 */
public class SpacLoadingStepRWP {
	
	
	private String filePath;
	
	@Bean
	/**
	 * from file get to data token
	 * @return
	 */
    public FlatFileItemReader<SpacFile> observedDataStepReader() {	
		//Create reader instance
        FlatFileItemReader<SpacFile> reader = new FlatFileItemReader<SpacFile>();
         
        //Set input file location
        reader.setResource(new FileSystemResource(filePath));
         
        //Set number of lines to skips. Use it if file has header rows.
        reader.setLinesToSkip(2);   
        //
        
        //Configure how each line will be parsed and mapped to different values
        
        reader.setLineMapper(new DefaultLineMapper<SpacFile>() {
            {
                //3 columns in each row
                setLineTokenizer(new DelimitedLineTokenizer() {
                    {
                        setNames(new String[] { "Date", "Station", "Type", "Valeur" });
                        setDelimiter(";");
                    }                    
                });
                
                //Set values in Employee class
                setFieldSetMapper(new SpacFileMapper());
            }
        });
        
        return reader;		 
    }
	
	@Bean
	/**
	 * save token to flow or memrory 
	 * @return
	 */
    public ItemWriter<SpacFile> weatherDataStepWriter() {
		
		return new WeatherDataStepWriter();
 
    }
	        
	public class WeatherDataStepWriter implements ItemWriter<SpacFile>{
		
		private StepExecution stepExecution;
				
		private IndarApiCallerImp indarApiCallerImp;		
		
		private String stationName;
		
		@Override
		public void write(List<? extends SpacFile> items) throws Exception {
			if(items==null)return ;
			List<SpacFile> liste = indarApiCallerImp.saveAllObservation((List<SpacFile>) items, stationName);	
		}
		
		@BeforeStep
	    public void getParams(StepExecution stepExecution) {
	        this.stepExecution = stepExecution;
	        
	        
	    }
		
		
		
	}      
}
