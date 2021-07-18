package com.apiweather.app.jobs;

import java.util.List;

import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

import com.apiweather.app.dss.DSSFileBuilder;
import com.apiweather.app.dss.DSSFileBuilderImp;
import com.apiweather.app.dss.DssBlocHeaderBuilder.TYPE_FILE;
import com.apiweather.app.jobs.domain.SpacFile;
import com.apiweather.app.jobs.domain.SpacFileMapper;

import lombok.Getter;
import lombok.Setter;



/**
 * @author a.bouabidi
 *
 */
@Component
@Getter
@Setter
public class WeatherDataStepRWP {
	
	
	@Value("${csv.input.path}")
	private String filePath="F:\\git\\apigeo\\hsm\\dss\\Export_SPAC_2021-04-02-17-03.csv";
	
	@Bean
	/**
	 * from file get to data token
	 * @return
	 */
    public FlatFileItemReader<SpacFile> weatherDataStepReader() {	
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
		
		
		private DSSFileBuilder dSSFileBuilderImp;
		
		
		
		@Override
		public void write(List<? extends SpacFile> items) throws Exception {
			DSSBlockData block = 
			if(items==null) return ;
			double[] tab= new double[items.size()];
			for (int i=0;i<items.size();i++) {
				
				tab[i] = items.get(i).getValeur();
				
				//System.out.println("size"+items.size());
				
			}
			if(this.dSSFileBuilderImp == null) {
				this.dSSFileBuilderImp = new DSSFileBuilderImp();
				this.dSSFileBuilderImp.init("F:/Workspaces/apigeo/apibatchs/MyDSS_file.dss", "F:/Workspaces/apigeo/apibatchs/logs/MyDSS_file.dss");
				this.dSSFileBuilderImp.create(TYPE_FILE.REGULAR_SERIES, "MOROCCO FLOOD HAZARD","BGE_EL_MELLAH","ET","01Nov1974","1DAY","EL MELLAH HMS");				
			}
			this.dSSFileBuilderImp.appendData(tab, "mm", "PER-CUM", 60*24);			
		}
		
		@BeforeStep
	    public void saveStepExecution(StepExecution stepExecution) {
	        this.stepExecution = stepExecution;
	    }
		
		@AfterStep
		public void closeAll() {
			dSSFileBuilderImp.close();
		}
		
	}
	
	
	
}
