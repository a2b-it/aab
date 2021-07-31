package com.apiweather.app.jobs;


import java.util.Arrays;
import java.util.List;

import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemStream;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.apiweather.app.dss.DSSFileBuilder;
import com.apiweather.app.dss.DSSFileBuilderImp;
import com.apiweather.app.dss.DssBlocHeaderBuilder.TYPE_FILE;
import com.apiweather.app.dss.model.DSSBlock;
import com.apiweather.app.dss.model.DSSBlockData;
import com.apiweather.app.dto.DSSBlockDataMapper;
import com.apiweather.app.jobs.domain.SpacFile;
import com.apiweather.app.jobs.domain.SpacFileMapper;
import com.apiweather.app.jobs.domain.Station;
import com.apiweather.app.jobs.domain.WeatherPrecip;
import com.apiweather.app.rest.client.IndarApiCaller;
import com.apiweather.app.rest.client.RestClientsFactory;

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
    public ItemReader<DSSBlock> weatherDataStepReader() {	
        return new WeatherDataStepReader();		 
    }
	
	@Bean
	/**
	 * save token to flow or memrory 
	 * @return
	 */
    public ItemWriter<DSSBlock> weatherDataStepWriter() {
		
		return new WeatherDataStepWriter();
 
    }
	@Scope("step")
	public class WeatherDataStepReader implements ItemReader<DSSBlock>, ItemStream{
		
		@Autowired
		private IndarApiCaller IndarApiCallerImp;
		
		private List<DSSBlock> dSSBlocks;
		
		private static final String CURRENT_INDEX = "current.index";
		
	    private int nextElementIndex;
	    

		@Override
		public DSSBlock read()
				throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
			//
			if (dssBlocksDataIsNotInitialized()) {
				dSSBlocks = fetchDSSBlocksDataFromAPI();
	        }
			// 
			DSSBlock nextDSSBlock = null;	 
	        if (nextElementIndex < dSSBlocks.size()) {
	        	nextDSSBlock = dSSBlocks.get(nextElementIndex);
	        	nextElementIndex++;
	        }
	        else {
	        	nextElementIndex = 0;
	        	dSSBlocks = null;
	        }
	 
	        return nextDSSBlock;
			
		}

		private List<DSSBlock> fetchDSSBlocksDataFromAPI () {
			//
			List<DSSBlock> liste = IndarApiCallerImp.processDssFileData(null);
		    return liste;
		}

		private boolean dssBlocksDataIsNotInitialized() {
	        return this.dSSBlocks == null;
	    }
		
		
		@Override
		public void open(ExecutionContext executionContext) throws ItemStreamException {
			if (executionContext.containsKey(CURRENT_INDEX)) {
				nextElementIndex = Long.valueOf(executionContext.getLong(CURRENT_INDEX)).intValue();
	        }
	        else {
	        	nextElementIndex = 0;
	        }
			
		}

		@Override
		public void update(ExecutionContext executionContext) throws ItemStreamException {
			executionContext.putLong(CURRENT_INDEX, Long.valueOf(nextElementIndex).longValue());
			
		}

		
		
		@Override
		public void close() throws ItemStreamException {}
		
	}
	
	@Scope("step")
	public class WeatherDataStepWriter implements ItemWriter<DSSBlock>{
		
		private StepExecution stepExecution;
		
		
		private DSSFileBuilder dSSFileBuilderImp;
		//"F:/Workspaces/apigeo/apibatchs/MyDSS_file.dss"
		private String dss_fileName;
		
		
		//F:/Workspaces/apigeo/apibatchs/logs/MyDSS_file.dss
		private String dss_log_fileName;
		
		
		@Override
		public void write(List<? extends DSSBlock> items) throws Exception {
			//DSSBlockData block = 
			if(items==null) return ;			
			for (DSSBlock block : items) {
				if(this.dSSFileBuilderImp == null) {
					this.dSSFileBuilderImp = new DSSFileBuilderImp();
					this.dSSFileBuilderImp.init(dss_fileName, dss_log_fileName);
					this.dSSFileBuilderImp.create(TYPE_FILE.REGULAR_SERIES, block.getLocation(),block.getName(),block.getDataParam(),block.getStartDate(),block.getTimeInterval(),block.getDescription());				
				}
				this.dSSFileBuilderImp.appendData(block.getDssBlockDataAsDouble(), block.getUnits(), block.getType(), block.getTimeIntervalAsInt());	
			}
		}
		
		@BeforeStep
	    public void saveStepExecution(StepExecution stepExecution) {
	        this.stepExecution = stepExecution;
	    }
		
		@AfterStep
		public void closeAll() {
			dSSFileBuilderImp.close();
		}
		
		@Value("#{jobParameters['dss_fileName']}")
		public void setPathDssFile (final String name) {
			this.dss_fileName=name;
		}
		
		@Value("#{jobParameters['dss_log_fileName']}")
		public void setPathLogDssFile (final String name) {
			this.dss_log_fileName=name;
		}
		
	}
	
	
	
}
