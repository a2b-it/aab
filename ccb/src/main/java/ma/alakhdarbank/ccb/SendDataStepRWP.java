/**
 * 
 */
package ma.alakhdarbank.ccb;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemStream;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ma.alakhdarbank.ccb.clients.ApiSendData;
import ma.alakhdarbank.ccb.entity.Lot;
import ma.alakhdarbank.ccb.exception.RCCBAppException;
import ma.alakhdarbank.ccb.persistence.LotRepository;
import ma.alakhdarbank.ccb.persistence.ServiceLot;
import ma.alakhdarbank.ccb.sec.FileEncrypterDecrypter;
import ma.alakhdarbank.ccb.sec.RSAKeyManager;
import ma.alakhdarbank.ccb.sec.SyncEncrypterDecrypter;

/**
 * @author a.bouabidi
 *
 */
@Component
@NoArgsConstructor
@Getter
@Setter
public class SendDataStepRWP {
	
	@Autowired
	private ApiSendData apiSendDataImp;
	
	@Value("${bkam.sendCCBData.filepath.url}")
	public String filepath;
	
	@Value("${bkam.auth.api.password}")
	public String password;
	
	@Value("${bkam.auth.api.login}")
	public String login;
	
	@Autowired
	private FileEncrypterDecrypter fileEncrypterDecrypterImp;
		
	@Autowired
	private SyncEncrypterDecrypter syncEncrypterDecrypterImp;
		
	@Autowired
	private RSAKeyManager rsaKeyManager;
	
	@Autowired
	private ServiceLot serviceLot;
	    		
	public String JSON_FILE_PATH="json.file.path";
	public String JSON_FILE_NAME="json.file.name";
	
	public String PASSWORD_HASH="password.hash";
	public String TOKEN="token";
	public String LOGIN="login";
	public String PASSWORD="password";
	public String PUB_CERT_PATH="pub.cert.path";
	
	public String ID_LOT="id.lot";
	public String NBR_CPT="nbr.cpt";
	
	@Bean
	@StepScope
	/**
	 * from rest get token
	 * @return
	 */
    public ItemReader<String> jsonFileReader() {	
		
		return new SendDataStepReader(serviceLot );
 
    }
	
	
	@Bean
	@StepScope
	/**
	 * from rest get token
	 * @return
	 */
    public ItemProcessor<String, String> jsonFileProcessor() {		
		return new SendDataStepProcessor( fileEncrypterDecrypterImp, syncEncrypterDecrypterImp, 
				rsaKeyManager);
 
    }
	
	@Bean
	@StepScope
	/**
	 * save token to flow or memrory 
	 * @return
	 */
    public ItemWriter<String> jsonFileWriter() {
		
		return new SendDataStepWriter(apiSendDataImp, serviceLot);
 
    }
	
	@Getter
	@Setter
	public class SendDataStepProcessor implements ItemProcessor<String, String>{
		
		private FileEncrypterDecrypter fileEncrypterDecrypterImp;
		
		
		private SyncEncrypterDecrypter syncEncrypterDecrypterImp;
		
		
		private RSAKeyManager rsaKeyManager;
		
		private String loginIn;
		
		private String passwordIn;
		
		private String pubCertPath;
		
		private String token;
		
		@BeforeStep
	    public void beforeStep(StepExecution stepExecution) throws RCCBAppException {
	       
	        JobParameters parameters = stepExecution.getJobExecution().getJobParameters();
	        
	        //this.loginIn = parameters.getString(LOGIN);
	        //this.passwordIn = parameters.getString(PASSWORD);
	        
	        this.pubCertPath = parameters.getString(PUB_CERT_PATH);
	        try {
				syncEncrypterDecrypterImp.init(pubCertPath);
				fileEncrypterDecrypterImp.init();
			} catch (RCCBAppException | InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidAlgorithmParameterException e1) {
				throw new RCCBAppException(e1);
			}
			
	        
	    }
		
		@AfterStep
	    public void afterStep(StepExecution stepExecution) throws RCCBAppException {	       
	        stepExecution.getExecutionContext().put(TOKEN, this.token);   	        
	    }
		
		
		@Override
		public String process(String item) throws Exception {						
			return encryptData (item); 
		}


		public SendDataStepProcessor(FileEncrypterDecrypter fileEncrypterDecrypterImp,
				SyncEncrypterDecrypter syncEncrypterDecrypterImp, RSAKeyManager rsaKeyManager) {
			super();
			this.fileEncrypterDecrypterImp = fileEncrypterDecrypterImp;
			this.syncEncrypterDecrypterImp = syncEncrypterDecrypterImp;
			this.rsaKeyManager = rsaKeyManager;
		}
		
		
		private String encryptData (String spath) throws RCCBAppException, InvalidKeyException {									
			byte[] ciphered = fileEncrypterDecrypterImp.encrypt(spath);
			try {
				byte[] key = syncEncrypterDecrypterImp.encrypt (fileEncrypterDecrypterImp.getSecretKey());
				this.token = syncEncrypterDecrypterImp.encodeData(key);
			} catch (InvalidKeyException | IllegalBlockSizeException e) {
				throw new RCCBAppException (e);
			}
			return syncEncrypterDecrypterImp.encodeData(ciphered);
		}
		
	}
	
	public class SendDataStepWriter implements ItemWriter<String>, ItemStream{
				
		private ApiSendData apiSendDataImp;
		
		
		private ServiceLot serviceLot;
		
		private String login="";
		
		private String password_hash="";
		
		private String token="";
		
		private Long idLot;
		
		private int nbrCpt;
		
		public SendDataStepWriter(ApiSendData apiSendDataImp, ServiceLot serviceLot) {
			super();
			this.apiSendDataImp = apiSendDataImp;
			this.serviceLot =  serviceLot;
			
		}
		
		@BeforeStep
	    public void saveStepExecution(StepExecution stepExecution) {	        
	        JobParameters parameters = stepExecution.getJobExecution().getJobParameters();
	        this.login = parameters.getString(LOGIN);
	        this.password_hash = parameters.getString(PASSWORD);
	        this.token = parameters.getString(TOKEN);
	        this.idLot = parameters.getLong(ID_LOT);
	        this.nbrCpt= parameters.getLong(NBR_CPT).intValue();
	    }

		@Override
		public void write(List<? extends String> items) throws Exception {
			//ExecutionContext stepContext = this.stepExecution.getExecutionContext();
			//stepContext.put("auth_token", items.get(0)); 	
			HashMap<String, String> headers = new HashMap<String, String>();
			//TODO addinh headers					
			Date date = new Date ();
			SimpleDateFormat f = new SimpleDateFormat("YYYY-MM-DD HH24:mm:ss");
			
			headers.put("serviceBAM", "CCB");
			headers.put("idLot", idLot.toString());
			headers.put("emetteur", "AAB");
			headers.put("recepteur", "001");
			headers.put("dateDeclaration", f.format(date));
			headers.put("nbrEnregistrement", String.valueOf(nbrCpt));			
			headers.put("login", this.login);
			headers.put("password_hash",this.password_hash);
			headers.put("token",this.token);
			
			apiSendDataImp.send((String)items.get(0), headers);
			//
			serviceLot.updateLotENVOYER(idLot.intValue(), date);
		}
		
		
		

		@Override
		public void open(ExecutionContext executionContext) throws ItemStreamException {
			
			
		}

		@Override
		public void update(ExecutionContext executionContext) throws ItemStreamException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void close() throws ItemStreamException {
			
		}
		
		
	}
	
	@Getter
	@Setter
	public class SendDataStepReader implements ItemReader<String>, ItemStream{

		private String path;
		
		private String filename;
		
		private int endRead=0;
		
		private ServiceLot serviceLot;
		
		private Lot lot;
		
		
		
		public SendDataStepReader(ServiceLot serviceLot) {
			super();
			this.serviceLot=serviceLot;
					
		}
		
		
		
		
		@BeforeStep
	    public void saveStepExecution(StepExecution stepExecution) throws RCCBAppException, InvalidKeyException {	        	        
			JobParameters parameters = stepExecution.getJobExecution().getJobParameters();
	        this.path = parameters.getString(JSON_FILE_PATH);
	        this.filename = parameters.getString(JSON_FILE_NAME);
	    }
		
		
	
		@Override
		public String read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
			
			if(this.endRead !=0 ) return null;				
			String content = encryptFileAsString(path, this.filename);
			ObjectMapper om = new ObjectMapper();
			ma.alakhdarbank.dto.Lot lot = om.readValue(content, ma.alakhdarbank.dto.Lot.class);
			int nbrEnr = Integer.valueOf(lot.getH07());
			SimpleDateFormat f = new SimpleDateFormat("YYYYMMDD");
			Date dateArrete = f.parse(lot.h03);
			this.lot = this.serviceLot.saveNewLotENVOYER(filename, nbrEnr, dateArrete);
			return content; 
		}

		public  String encryptFileAsString(String dir, String filename)throws Exception
	    {
			this.endRead++;
	        return new String(Files.readAllBytes(Paths.get(dir+File.separator+filename)));
	    }


		@Override
		public void open(ExecutionContext executionContext) throws ItemStreamException {
			
		}


		@Override
		public void update(ExecutionContext executionContext) throws ItemStreamException {
			
			executionContext.put(LOGIN, login);
			executionContext.put(PASSWORD_HASH, password);
			executionContext.put(ID_LOT, lot.getId());
			executionContext.put(NBR_CPT, lot.getNbrCpt());
		}


		@Override
		public void close() throws ItemStreamException {
			
			
		}
	
	}
}

