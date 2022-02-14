/**
 * 
 */
package com.apiweather.app.jobs;

import java.io.IOException;
import java.util.List;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemStream;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.apiweather.app.dss.DSSFileBuilder;
import com.apiweather.app.dss.DSSFileBuilderImp;
import com.apiweather.app.dss.DssBlocHeaderBuilder.TYPE_FILE;
import com.apiweather.app.dss.model.DSSBlock;
import com.apiweather.app.dss.model.DSSBlockData;
import com.apiweather.app.dss.model.DSSFile;
import com.apiweather.app.dss.read.DSSFileReader;
import com.apiweather.app.excep.DSSBuildingException;
import com.apiweather.app.excep.DSSReadingException;
import com.apiweather.app.excep.DSSWritingException;
import com.apiweather.app.rest.client.IndarApiCaller;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;
import lombok.Setter;

/**
 * @author a.bouabidi
 *
 */
@Component
@Getter
@Setter
public class DssFileStepRWP {

	

	@Autowired
	private IndarApiCaller indarApiCallerImp;
	
	
	/*
	 * @Bean
	 *//**
		 * save token to flow or memrory
		 * 
		 * @return
		 *//*
			 * public ItemReader<DSSBlockData> dssFileDataStepReader() throws IOException {
			 * Resource[] resources = null; ResourcePatternResolver patternResolver = new
			 * PathMatchingResourcePatternResolver(); try { resources =
			 * patternResolver.getResources("F:/WORK/hms/input/*.csv"); } catch (IOException
			 * e) { throw e; } MultiResourceItemReader<DSSBlockData> reader = new
			 * MultiResourceItemReader<>(); reader.setResources (resources);
			 * //reader.setDelegate (fileReader()); return reader;
			 * 
			 * }
			 */

	public ItemReader<DSSBlock> dssFileDataStepReader(DSSFileReader dSSFileReaderImp) throws IOException {
		
		return new DssFileStepReader(dSSFileReaderImp);

	}
	
	
	public ItemReader<DSSBlock> dssApiDataStepReader () {
		return null;
	}
	
	public ItemWriter<DSSBlock> dssApiDataStepWriter () {
		return new DssApiStepWriter(indarApiCallerImp);
	}
	
/*
	private FlatFileItemReader<DSSBlockData> fileReader() {
		//
		FlatFileItemReader<DSSBlockData> reader = new FlatFileItemReader<DSSBlockData>();
		// Set input file location
		// reader.setResource(new FileSystemResource(filePath));

		// Set number of lines to skips. Use it if file has header rows.
		reader.setLinesToSkip(7);
		//

		// Configure how each line will be parsed and mapped to different values
		reader.setLineMapper(new DefaultLineMapper<DSSBlockData>() {
			{
				// 3 columns in each row
				setLineTokenizer(new DelimitedLineTokenizer() {
					{
						setNames(new String[] { "Index", "Date", "Valeur" });
						setDelimiter(";");
					}
				});
				// Set values in Employee class
				setFieldSetMapper(new DSSBlockDataDbMapper());
			}
		});

		return reader;
	}
*/
	@Bean
	/**
	 * save token to flow or memrory
	 * 
	 * @return
	 */
	public ItemWriter<DSSBlock> dssFileDataStepWriter(DSSFileBuilder dSSFileBuilderImp) {

		return new DssFileStepWriter(dSSFileBuilderImp);

	}
	
	@Bean
	/**
	 * save token to flow or memrory
	 * 
	 * @return
	 */
	public ItemWriter<DSSBlock> dssFileDataStepWriterToconsole() {

		return new DssFileWriteToconsole();

	}
	
	@Getter
	@Setter
	public class DssFileWriteToconsole implements ItemWriter<DSSBlock>{
		private String stationName;				
		
		private DSSFile dssfile;
		
		@Override
		public void write(List<? extends DSSBlock> items) throws Exception {
			if (items == null) {
				System.out.println("items is null ");
				return;
			}
			DSSBlock[] tab = new DSSBlock [items.size()]; 
			items.toArray(tab);
			dssfile.setBlocks(tab);
			ObjectMapper mapper = new ObjectMapper();
			String json = mapper.writeValueAsString(dssfile);
			System.out.println("Size "+ items.size());
			System.out.println("Items "+ json.substring(0, 1000));
			
		}
		
		@BeforeStep
	    public void saveStepExecution(StepExecution stepExecution) {	        
	        JobParameters parameters = stepExecution.getJobExecution().getJobParameters();
	        String filepath = parameters.getString("dss.file.path");
	        String filename = parameters.getString("dss.file.filename");	        
	        String logpath  = parameters.getString("dss.log.path");
	        this.stationName = parameters.getString("dss.file.station");
	        this.dssfile = new DSSFile();
			dssfile.setFilename(filename);
			dssfile.setFilepath(filepath);
			dssfile.setLogpath(logpath);
	    }
		
	}

	@Getter
	@Setter
	public class DssFileStepReader implements ItemReader<DSSBlock>, ItemStream {

		private DSSFile dssfile ;
		
		private DSSFileReader dSSFileReaderImp ;

		private DSSBlock[] blocks;

		private static final String CURRENT_INDEX = "current.index";

		private int nextElementIndex;
		
		
		
		
		@BeforeStep
	    public void saveStepExecution(StepExecution stepExecution) {	        
	        JobParameters parameters = stepExecution.getJobExecution().getJobParameters();
	        
	        String filepath = parameters.getString("dss.file.path");
	        String filename = parameters.getString("dss.file.filename");	        
	        String logpath  = parameters.getString("dss.log.path");
	        //this.stationName = parameters.getString("dss.file.station");
	        this.dssfile = new DSSFile();
			dssfile.setFilename(filename);
			dssfile.setFilepath(filepath);
			dssfile.setLogpath(logpath);
	    }
		
		/**
		 * read data for one year
		 */
		@Override
		public DSSBlock read()
				throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {

			if (dssBlocksDataIsNotInitialized()) {
				blocks = fetchDSSBlocksDataFromFile();
			}
			//
			DSSBlock nextDSSBlock = null;
			if (nextElementIndex < blocks.length) {
				nextDSSBlock = blocks[nextElementIndex];
				nextElementIndex++;
			} else {
				nextElementIndex = 0;
				blocks = null;
				return null;
			}

			return nextDSSBlock;
		}

		private DSSBlock[] fetchDSSBlocksDataFromFile() throws DSSReadingException {
			/*DSSFile dssfile = new DSSFile();
			dssfile.setFilename(dssFileFullPath);
			dssfile.setLogpath(dssLogFileFullPath);*/
			//
			// Create reader instance
			StringBuilder b = new StringBuilder();
			b.append(dssfile.getFilepath()).append(dssfile.getFilename());
			
			dSSFileReaderImp.init(b.toString(), DSSFileReader.TYPE_FILE.REGULAR_TIME_SERIES, dssfile.getLogpath());
			DSSBlock[] block = dSSFileReaderImp.read();
			return block;
		}

		private boolean dssBlocksDataIsNotInitialized() {
			return this.blocks == null;
		}

		@Override
		public void open(ExecutionContext executionContext) throws ItemStreamException {
			if (executionContext.containsKey(CURRENT_INDEX)) {
				nextElementIndex = Long.valueOf(executionContext.getLong(CURRENT_INDEX)).intValue();
			} else {
				nextElementIndex = 0;
			}

		}

		@Override
		public void update(ExecutionContext executionContext) throws ItemStreamException {
			executionContext.putLong(CURRENT_INDEX, Long.valueOf(nextElementIndex).longValue());

		}

		@Override
		public void close() throws ItemStreamException {
			int status = dSSFileReaderImp.close();
			if (status != 0)
				dSSFileReaderImp.logStatus();

		}

		public DssFileStepReader(DSSFileReader dSSFileReaderImp) {
			super();
			this.dSSFileReaderImp = dSSFileReaderImp;
		}

	}

	@Getter
	@Setter
	public class DssFileStepWriter implements ItemWriter<DSSBlock> {

		private DSSFileBuilder dSSFileBuilderImp;
		
		private DSSFile dssfile;
		
		@Override
		public void write(List<? extends DSSBlock> items) throws Exception {
			if (items == null)
				return;
			double[] tab = new double[items.size()];
			// TODO to be reviewed
			init(items.get(0), dssfile);
			for (int j = 0; j < items.size(); j++) {
				DSSBlock block = items.get(j);
				int i = 0;
				for (DSSBlockData data : block.getDssBlockDatas()) {
					tab[i] = data.getValue();
					// System.out.println("size"+items.size());
				}
				this.dSSFileBuilderImp.appendData(tab, block.getUnits(), block.getType(),
						Integer.valueOf(block.getTimeInterval()));
			}

		}

		private void init(DSSBlock block, DSSFile file) throws DSSBuildingException {
			if (block == null) {
				throw new DSSBuildingException("Block is null");
			}
			if (this.dSSFileBuilderImp == null) {
				this.dSSFileBuilderImp = new DSSFileBuilderImp();
				this.dSSFileBuilderImp.init(file.getFilepath()+file.getFilename(), file.getLogpath());
				this.dSSFileBuilderImp.create(TYPE_FILE.REGULAR_SERIES, block.getName(), block.getLocation(),
						block.getDataParam(), block.getStartDate(), block.getTimeInterval(), block.getDescription());
			}

		}

		public DssFileStepWriter(DSSFileBuilder dSSFileBuilderImp) {
			super();
			this.dSSFileBuilderImp = dSSFileBuilderImp;
		}
		
		@BeforeStep
	    public void saveStepExecution(StepExecution stepExecution) {	        
	        JobParameters parameters = stepExecution.getJobExecution().getJobParameters();
	        String filepath = parameters.getString("dss.file.path");
	        String filename = parameters.getString("dss.file.filename");	        
	        String logpath  = parameters.getString("dss.log.path");
	        this.dssfile = new DSSFile();
			dssfile.setFilename(filename);
			dssfile.setFilepath(filepath);
			dssfile.setLogpath(logpath);
	    }
	}

	@Getter
	@Setter
	public class DssApiStepWriter implements ItemWriter<DSSBlock> {
		
		private IndarApiCaller indarApiCallerImp;
		
		private String stationName;				
		
		private DSSFile dssfile;
		
		public DssApiStepWriter(IndarApiCaller indarApiCallerImp) {
			super();
			this.indarApiCallerImp = indarApiCallerImp;
		}



		@Override
		public void write(List<? extends DSSBlock> items) throws Exception {
			DSSBlock[] tab = new DSSBlock [items.size()]; 
			items.toArray(tab);
			dssfile.setBlocks(tab);
			if (!indarApiCallerImp.saveAllDssData(dssfile, stationName)) {
				throw new DSSWritingException("Data not persisted");
			};
			
			
		}
		
		@BeforeStep
	    public void saveStepExecution(StepExecution stepExecution) {	        
	        JobParameters parameters = stepExecution.getJobExecution().getJobParameters();
	        String filepath = parameters.getString("dss.file.path");
	        String filename = parameters.getString("dss.file.filename");	        
	        String logpath  = parameters.getString("dss.log.path");
	        this.stationName = parameters.getString("dss.file.station");
	        this.dssfile = new DSSFile();
			dssfile.setFilename(filename);
			dssfile.setFilepath(filepath);
			dssfile.setLogpath(logpath);
	    }

		
	}
}
