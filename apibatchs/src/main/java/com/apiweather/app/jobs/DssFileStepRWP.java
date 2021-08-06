/**
 * 
 */
package com.apiweather.app.jobs;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import com.apiweather.app.dss.DSSFileBuilder;
import com.apiweather.app.dss.DSSFileBuilderImp;
import com.apiweather.app.dss.DssBlocHeaderBuilder.TYPE_FILE;
import com.apiweather.app.dss.model.DSSBlock;
import com.apiweather.app.dss.model.DSSBlockData;
import com.apiweather.app.dss.model.DSSBlockDataDbMapper;
import com.apiweather.app.dss.model.DSSFile;
import com.apiweather.app.excep.DSSBuildingException;

/**
 * @author a.bouabidi
 *
 */

public class DssFileStepRWP {

	private String dssFilePath;

	private String logFilePath;

	@Bean
	/**
	 * save token to flow or memrory
	 * 
	 * @return
	 */
	public ItemReader<DSSBlockData> dssFileDataStepReader() throws IOException {
		Resource[] resources = null;
		ResourcePatternResolver patternResolver = new PathMatchingResourcePatternResolver();
		try {
			resources = patternResolver.getResources("F:/WORK/hms/input/*.csv");
		} catch (IOException e) {
			throw e;
		}
		MultiResourceItemReader<DSSBlockData> reader = new MultiResourceItemReader<>();
		reader.setResources (resources);
		//reader.setDelegate (fileReader());
		return reader;

	}

	@Value("#{jobParameters['dssfileName']}")
	public void setFileName(final String name) {
		this.dssFilePath = name;
	}

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

	@Bean
	/**
	 * save token to flow or memrory
	 * 
	 * @return
	 */
	public ItemWriter<DSSBlock> weatherDataStepWriter() {

		return new DssFileStepWriter();

	}

	public class DssFileStepReader implements ItemReader<DSSBlock> {

		private StepExecution stepExecution;
		
		private String dirpath;
		
		
		/**
		 * read data for one year
		 */
		@Override
		public DSSBlock read()
				throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
			DSSFile dssfile = new DSSFile();
			dssfile.setFilename("F:/Workspaces/apigeo/apibatchs/MyDSS_file.dss");
			dssfile.setLogpath("F:/Workspaces/apigeo/apibatchs/logs/MyDSS_file.log");

			// Create reader instance
			
			
			return null;
		}

		@BeforeStep
		public void saveStepExecution(StepExecution stepExecution) {
			this.stepExecution = stepExecution;
		}
	}

	public class DssFileStepWriter implements ItemWriter<DSSBlock> {

		private DSSFileBuilder dSSFileBuilderImp;

		@Override
		public void write(List<? extends DSSBlock> items) throws Exception {
			if (items == null)
				return;
			double[] tab = new double[items.size()];
			//TODO to be reviewed
			init(items.get(0));
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

		private void init(DSSBlock block) throws DSSBuildingException {
			if (block == null) {
				throw new DSSBuildingException("Block is null");
			}			
			if (this.dSSFileBuilderImp == null) {
				this.dSSFileBuilderImp = new DSSFileBuilderImp();
				this.dSSFileBuilderImp.init(block.getDssFile().getFilename(), block.getDssFile().getLogpath());
				this.dSSFileBuilderImp.create(TYPE_FILE.REGULAR_SERIES, block.getName(), block.getLocation(),
						block.getDataParam(), block.getStartDate(), block.getTimeInterval(), block.getDescription());
			}

		}

	}

	public interface FlatFileHeaderCallback {
		void writeFooter(Writer writer) throws IOException;
	}
}
