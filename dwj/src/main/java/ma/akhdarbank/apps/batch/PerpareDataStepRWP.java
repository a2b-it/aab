package ma.akhdarbank.apps.batch;

import java.util.List;

import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Autowired;

import ma.akhdarbank.apps.clients.ApiAuthClient;
import ma.akhdarbank.apps.dao.TierRepository;

public class PerpareDataStepRWP {

	public class PerpareDataReader implements ItemReader<String> {

		@Autowired
		TierRepository tierRepository;

		@Override
		public String read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
			return ;
		}

	}

	public class AuthStepWriter implements ItemWriter<String> {

		private StepExecution stepExecution;

		@Override
		public void write(List<? extends String> items) throws Exception {
			ExecutionContext stepContext = this.stepExecution.getExecutionContext();
			stepContext.put("auth_token", items.get(0));
		}

		@BeforeStep
		public void saveStepExecution(StepExecution stepExecution) {
			this.stepExecution = stepExecution;
		}

	}
	/*
	 * @BeforeStep public void retrieveInterstepData(StepExecution stepExecution) {
	 * JobExecution jobExecution = stepExecution.getJobExecution(); ExecutionContext
	 * jobContext = jobExecution.getExecutionContext(); this.someObject =
	 * jobContext.get("someKey"); }
	 */
}
