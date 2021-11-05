/**
 * 
 */
package ma.alakhdarbank.ccb;


import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;
import lombok.Setter;
import ma.alakhdarbank.ccb.clients.ApiReadCTR;
import ma.alakhdarbank.ccb.entity.Ctr;
import ma.alakhdarbank.ccb.entity.Lot;
import ma.alakhdarbank.ccb.persistence.ServiceLot;
import ma.alakhdarbank.ccb.sec.FileEncrypterDecrypter;

/**
 * @author a.bouabidi
 *
 */

public class ReadCTRRWP {
	
	public String JSON_FILE_PATH="json.file.path";
	public String JSON_FILE_NAME="json.file.name";
	public String PASSWORD_HASH="password.hash";
	public String TOKEN="token";
	public String LOGIN="login";
	public String PASSWORD="password";
	
	@Autowired
	private ApiReadCTR apiReadCTRImp;
	
	@Autowired
	private ServiceLot serviceLot;
	
	private FileEncrypterDecrypter fileEncrypterDecrypter;
	
	@Bean	
	/**
	 * from rest get token
	 * @return
	 */
    public ItemReader<String> ctrFileReader() {	
		
		return new ReadCTRStepReader(apiReadCTRImp, serviceLot);
 
    }
	
	
	@Bean	
	/**
	 * from rest get token
	 * @return
	 */
    public ItemProcessor<String, Ctr> jsonCtrProcessor() {	
		
		return new ReadCTRStepProcessor();
 
    }
	
	@Bean	
	/**
	 * from rest get token
	 * @return
	 */
    public ItemWriter<Ctr> ctrFileWriter() {	
		
		return new ReadCTRStepWriter(serviceLot);
 
    }
	
	@Getter
	@Setter
	public class ReadCTRStepProcessor implements ItemProcessor<String, Ctr> {
		
		
		
		
		public ReadCTRStepProcessor() {
			super();			
		}



		@Override
		public Ctr process(String item) throws Exception {
			
			
			
			ObjectMapper om = new ObjectMapper();
			Ctr lot = om.readValue(item, Ctr.class);
			
			return lot;
		}
		
	}
	
	
	@Getter
	@Setter
	public class ReadCTRStepReader implements ItemReader<String>{
		
		private ApiReadCTR apiReadCTRImp;		
		
		private String login, password_hash, token;
		
		private Lot lot;
		
		private ServiceLot serviceLot;

		public ReadCTRStepReader(ApiReadCTR apiReadCTRImp, ServiceLot serviceLot) {
			super();
			this.apiReadCTRImp = apiReadCTRImp;
			this.serviceLot = serviceLot;
		}

		@BeforeStep
	    public void saveStepExecution(StepExecution stepExecution) {	        
	        JobParameters parameters = stepExecution.getJobExecution().getJobParameters();
	        this.login = parameters.getString(LOGIN);
	        this.password_hash = parameters.getString(PASSWORD);
	        this.token = parameters.getString(TOKEN);
	        this.lot = serviceLot.getLastLotEnvoyer();
	    }

		

		@Override
		public String read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
			Map<String, String> headers = new HashMap<String, String>();
			SimpleDateFormat f = new SimpleDateFormat("YYYYMMDD");
			if(this.lot == null ) return null;			
			headers.put("serviceBAM", "CCB");
			headers.put("idLot", lot.getIdLot().toString());
			headers.put("emetteur", "AAB");
			headers.put("recepteur", "001");
			headers.put("dateArrete", f.format(lot.getDateArrete ()));
			headers.put("password_hash", password_hash);
			headers.put("login", login);
			headers.put("token", token);
			
			String jsonCtr = apiReadCTRImp.read(headers);
			//
			
			
			
			return jsonCtr;
		}
		
	}

	public class ReadCTRStepWriter implements ItemWriter<Ctr> {

		private ServiceLot serviceLot;
		
		
		
		
		public ReadCTRStepWriter(ServiceLot serviceLot) {
			super();
			this.serviceLot = serviceLot;
		}




		@Override
		public void write(List<? extends Ctr> items) throws Exception {
			//TODO loop for ctr
			this.serviceLot.saveNewCtrLot(items.get(0));
		}
		
	}
}
