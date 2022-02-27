/**
 * 
 */
package com.apiweather.app.jobs;

import java.text.ParseException;
import java.util.List;

import org.springframework.batch.core.StepExecution;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import com.apiweather.app.excep.ApiBatchException;
import com.apiweather.app.jobs.SpacLoadingStepRWP.WeatherDataStepWriter;
import com.apiweather.app.jobs.domain.BCRFile;
import com.apiweather.app.jobs.domain.BCRFileMapper;
import com.apiweather.app.jobs.domain.SpacFile;
import com.apiweather.app.jobs.domain.SpacFileMapper;
import com.apiweather.app.rest.client.IndarApiCallerImp;

import lombok.Getter;
import lombok.Setter;

/**
 * @author a.bouabidi
 *
 */

@Component
@Getter
@Setter
public class BCRLoandingRWP {
	
	
	private String filePath;
	
	@Autowired
	private IndarApiCallerImp indarApiCallerImp;
	
	
	@Bean
	/**
	 * from file get to data 
	 * @return
	 */
    public FlatFileItemReader<BCRFile> preciptDataStepReader() throws ApiBatchException {	
		//Create reader instance
        FlatFileItemReader<BCRFile> reader = new FlatFileItemReader<BCRFile>();
         
        //Set input file location
        Resource ressource = new FileSystemResource(filePath);
        reader.setResource(ressource);
         
        //Set number of lines to skips. Use it if file has header rows.
        reader.setLinesToSkip(0);   
        //
        
        //Configure how each line will be parsed and mapped to different values
        
        try {
			reader.setLineMapper(new DefaultLineMapper<BCRFile>() {
			    {
			        //3 columns in each row
			        setLineTokenizer(new DelimitedLineTokenizer() {
			            {
			                setNames(new String[] { "lat", "long", "pluie"});
			                setDelimiter(" ");
			            }                    
			        });
			        
			        //Set values in Employee class
			        setFieldSetMapper(new BCRFileMapper(ressource.getFilename()));
			    }
			});
		} catch (ParseException e) {
			throw new ApiBatchException(e);
		}
        
        return reader;		 
    }
	
	
	@Bean
	/**
	 * save token to flow or memrory 
	 * @return
	 */
    public ItemWriter<BCRFile> BCRLoadingWriter() {
		
		return new BCRLoadingWriter(indarApiCallerImp);
 
    }
	
	public class BCRLoadingWriter implements ItemWriter<BCRFile>{

		private StepExecution stepExecution;
		
		private IndarApiCallerImp indarApiCallerImp;	
		
		

		public BCRLoadingWriter(IndarApiCallerImp indarApiCallerImp) {
			super();
			this.indarApiCallerImp = indarApiCallerImp;
		}



		@Override
		public void write(List<? extends BCRFile> items) throws Exception {
			// TODO Auto-generated method stub
			
		}
		
	}

}
