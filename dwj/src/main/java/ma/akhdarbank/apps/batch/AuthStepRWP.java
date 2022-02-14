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
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import ma.akhdarbank.apps.clients.ApiAuthClient;

@Component
public class AuthStepRWP {
	
	final String AUTH_TOKEN="auth_token";
	
	@Autowired
	private ApiAuthClient apiAuthClientImp;
	
	
	@Bean
	/**
	 * from rest get token
	 * @return
	 */
    public ItemReader<String> authReader() {	
		return new AuthStepReader(apiAuthClientImp);
 
    }
	
	@Bean
	/**
	 * save token to flow or memrory 
	 * @return
	 */
    public ItemWriter<String> authWriter() {
		
		return new AuthStepWriter();
 
    }

	public class AuthStepReader implements ItemReader<String>{
		
		
		ApiAuthClient apiAuthClientImp;
		
		private StepExecution stepExecution;
		
		@Autowired
		public AuthStepReader(ApiAuthClient apiAuthClientImp) {
			super();
			this.apiAuthClientImp = apiAuthClientImp;
		}


		@Override
		public String read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {			
			String token = (String) this.stepExecution.getJobExecution().getExecutionContext().get(AUTH_TOKEN);
			if (token != null) return null;
			return apiAuthClientImp.auth();
		}
		
		@BeforeStep
	    public void saveStepExecution(StepExecution stepExecution) {
	        this.stepExecution = stepExecution;
	    }
		
	}
	
	public class AuthStepWriter implements ItemWriter<String>{
		
		private StepExecution stepExecution;
		
		@Override
		public void write(List<? extends String> items) throws Exception {
			this.stepExecution.getJobExecution().getExecutionContext().put(AUTH_TOKEN, items.get(0));
			//stepContext.put(AUTH_TOKEN, items.get(0)); 			
		}
		
		@BeforeStep
	    public void saveStepExecution(StepExecution stepExecution) {
	        this.stepExecution = stepExecution;	        
	    }
		
	}
	
	
	
}
