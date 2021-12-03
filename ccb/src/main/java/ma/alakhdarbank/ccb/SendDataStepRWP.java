/**
 * 
 */
package ma.alakhdarbank.ccb;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
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
	
	@Value("${bkam.public_key.path}")
	public String publicKeyPath;
	
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
	public String PUBLIC_KEY_PATH="public_key.path";
	
	public String PASSWORD_HASH="password.hash";
	public String TOKEN="token";
	public String LOGIN="login";	
	public String PUB_CERT_PATH="pub.cert.path";
	
	public String ID_LOT="id.lot";
	public String NBR_CPT="nbr.cpt";
	
	@Bean
	
	/**
	 * from rest get token
	 * @return
	 */
    public ItemReader<String> jsonFileReader() {	
		
		return new SendDataStepReader(serviceLot);
 
    }
	
	
	@Bean
	
	/**
	 * from rest get token
	 * @return
	 */
    public ItemProcessor<String, String> jsonFileProcessor() {		
		return new SendDataStepProcessor( fileEncrypterDecrypterImp, syncEncrypterDecrypterImp, 
				rsaKeyManager);
 
    }
	
	@Bean
	
	/**
	 * save token to flow or memrory 
	 * @return
	 */
    public ItemWriter<String> jsonFileWriter() {
		
		return new SendDataStepWriter(apiSendDataImp, serviceLot);
 
    }
	
	@Getter
	@Setter	
	public class SendDataStepProcessor implements ItemProcessor<String, String>, ItemStream{
		
		private FileEncrypterDecrypter fileEncrypterDecrypterImp;
		
		
		private SyncEncrypterDecrypter syncEncrypterDecrypterImp;
		
		
		private RSAKeyManager rsaKeyManager;
		
		private String tokenValue;
		
		
		private ExecutionContext executionContext;
		
		/*@AfterStep
	    public void afterStep(StepExecution stepExecution) throws RCCBAppException {	       
	        stepExecution.getExecutionContext().put(TOKEN, this.token);   	        
	    }*/
		
		
		@Override
		public String process(String item) throws Exception {		
			System.out.println("Encrypt Data:["+item+"]");
			String r = encryptData (item);
			System.out.println("Token:["+getTokenValue()+"]");
			System.out.println("Encrypted Data:["+r+"]");
			executionContext.putString(TOKEN, getTokenValue());
			return r;
			
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
				this.tokenValue = syncEncrypterDecrypterImp.encodeData(key);
			} catch (InvalidKeyException | IllegalBlockSizeException e) {
				throw new RCCBAppException (e);
			}
			return syncEncrypterDecrypterImp.encodeData(ciphered);
		}

		@Override
		public void open(ExecutionContext executionContext) throws ItemStreamException {
			this.executionContext =  executionContext;
			//JobParameters parameters = stepExecution.getJobExecution().getJobParameters();
			System.out.println("================== open processor");	       
			String public_secrt= executionContext.getString(PUB_CERT_PATH);
			//TODO throw exception if public_secrt is null 
	        try {
				syncEncrypterDecrypterImp.init(public_secrt);
				fileEncrypterDecrypterImp.init();
			} catch (RCCBAppException | InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidAlgorithmParameterException e1) {
				throw new ItemStreamException(new RCCBAppException(e1));
			}
			
		}

		@Override
		public void update(ExecutionContext executionContext) throws ItemStreamException {
			System.out.println("================== update processor");		
		}

		@Override
		public void close() throws ItemStreamException {
			
		}
		
	}
	
	@Getter
	@Setter	
	public class SendDataStepWriter implements ItemWriter<String>, ItemStream{
				
		private ApiSendData apiSendDataImp;
		
		
		private ServiceLot serviceLot;
		
		private ExecutionContext executionContext;
		
		private String token="";
		
		private Long idLot;
		
		private Long nbrCpt;
		
		public SendDataStepWriter(ApiSendData apiSendDataImp, ServiceLot serviceLot) {
			super();
			this.apiSendDataImp = apiSendDataImp;
			this.serviceLot =  serviceLot;
			
		}
		/*
		@BeforeStep
	    public void saveStepExecution(StepExecution stepExecution) {	        
	        JobParameters parameters = stepExecution.getJobExecution().getJobParameters();
	        this.login = parameters.getString(LOGIN);
	        this.password_hash = parameters.getString(PASSWORD);
	        this.token = parameters.getString(TOKEN);
	        this.idLot = parameters.getLong(ID_LOT);
	        this.nbrCpt= parameters.getLong(NBR_CPT).intValue();
	    }*/

		@Override
		public void write(List<? extends String> items) throws Exception {
			System.out.println("================== write");
			//ExecutionContext stepContext = this.stepExecution.getExecutionContext();
			//stepContext.put("auth_token", items.get(0)); 	
			HashMap<String, String> headers = new HashMap<String, String>();
			//TODO addinh headers		
			Long idLot = executionContext.getLong(ID_LOT);
			Date date = new Date ();
			SimpleDateFormat f = new SimpleDateFormat("YYYY-MM-DD HH24:mm:ss");
			
			headers.put("serviceBAM", "CCB");
			headers.put("idLot", idLot.toString());
			headers.put("emetteur", "AAB");
			headers.put("recepteur", "001");
			headers.put("dateDeclaration", f.format(date));
			headers.put("nbrEnregistrement", String.valueOf(nbrCpt));			
			headers.put("login", getLogin());
			headers.put("password_hash",getPassword());
			headers.put("token",this.token);
			
			apiSendDataImp.send((String)items.get(0), headers);
			//
			serviceLot.updateLotENVOYER(idLot, date);
		}
		
		
		

		@Override
		public void open(ExecutionContext executionContext) throws ItemStreamException {
			// JobParameters parameters = executionContext.get
			this.executionContext = executionContext;
			
			System.out.println("================== open writer");
			//this.login = (executionContext.containsKey(LOGIN))?executionContext.getString(LOGIN):null;
	        //this.password_hash =(executionContext.containsKey(PASSWORD_HASH))? executionContext.getString(PASSWORD_HASH):null;
	        /*this.token = (executionContext.containsKey(TOKEN))? executionContext.getString(TOKEN):null;
	        
	        
	        this.idLot = (executionContext.containsKey(ID_LOT))? executionContext.getLong(ID_LOT):null; 
	        this.nbrCpt=  (executionContext.containsKey(NBR_CPT))?executionContext.getLong(NBR_CPT):null;
			*/
		}

		@Override
		public void update(ExecutionContext executionContext) throws ItemStreamException {
			// TODO Auto-generated method stub
			System.out.println("================== update writer");
		}

		@Override
		public void close() throws ItemStreamException {
			
		}
		
		
	}
	
	@Getter
	@Setter	
	public class SendDataStepReader implements ItemReader<String>{

		private String path;
		
		private String filename;
		
		private String publicCert;
		
		private int index=0;
		
		private ServiceLot serviceLot;
		
		private Lot lot;
		
		private ExecutionContext executionContext;
		
		private File[] jsons;
		
		public SendDataStepReader(ServiceLot serviceLot) {
			super();
			this.serviceLot=serviceLot;
	
			
		}
		
		
		
		
		@BeforeStep
	    public void saveStepExecution(StepExecution stepExecution) throws RCCBAppException, InvalidKeyException {	        	        
			JobParameters parameters = stepExecution.getJobExecution().getJobParameters();
	        this.path = getFilepath();
	        this.filename = getFilename();
	        this.publicCert = getPublicKeyPath();
	        
	        // save variable to context
	        this.executionContext = stepExecution.getExecutionContext();
	        this.executionContext.putString(PUB_CERT_PATH, getPublicCert());
	    }
		
		
	
		@Override
		public String read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
			System.out.println("================== read .json files");
			if(this.jsons ==null ) {
				this.jsons = new File(path).listFiles(new FilenameFilter() {
					
					@Override
					public boolean accept(File dir, String name) {
						 return name.toLowerCase().endsWith(".json");
					}
				});
			}else if(index >= jsons.length){
				return null;
			}
			
			File currentFile = jsons[index++];
			String content = encryptFileAsString(currentFile.getParent(), currentFile.getName());
			ObjectMapper om = new ObjectMapper();
			ma.alakhdarbank.dto.Lot lot = om.readValue(content, ma.alakhdarbank.dto.Lot.class);
			int nbrEnr = Integer.valueOf(lot.getH07());
			SimpleDateFormat f = new SimpleDateFormat("YYYYMMDD");
			Date dateArrete = f.parse(lot.h03);
			this.lot = this.serviceLot.saveNewLotENVOYER(filename, nbrEnr, dateArrete);
			//
			executionContext.putLong(ID_LOT, this.lot.getId());
			executionContext.putLong(NBR_CPT, this.lot.getNbrCpt());			
			//
			return content; 
		}

		public  String encryptFileAsString(String dir, String filename)throws Exception
	    {			
	        return new String(Files.readAllBytes(Paths.get(dir+File.separator+filename)));
	    }


	
	}
}


