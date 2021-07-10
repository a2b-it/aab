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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import ma.akhdarbank.apps.clients.ApiAuthClient;

@Component
public class AuthStepRWP {
	
	@Autowired
	private ApiAuthClient apiAuthClientImp;
	
	
	@Bean
	/**
	 * from rest get token
	 * @return
	 */
    public ItemReader<String> reader() {	
		return new AuthStepReader(apiAuthClientImp);
 
    }
	
	@Bean
	/**
	 * save token to flow or memrory 
	 * @return
	 */
    public ItemWriter<String> writer() {
		
		return new AuthStepWriter();
 
    }

	public class AuthStepReader implements ItemReader<String>{
		
		
		ApiAuthClient apiAuthClientImp;
		
		
		@Autowired
		public AuthStepReader(ApiAuthClient apiAuthClientImp) {
			super();
			this.apiAuthClientImp = apiAuthClientImp;
		}



		@Override
		public String read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {			
			return apiAuthClientImp.auth();
		}
		
	}
	
	public class AuthStepWriter implements ItemWriter<String>{
		
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
	
	
	
}
