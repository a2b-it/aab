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
import java.security.InvalidKeyException;
import java.util.HashMap;
import java.util.List;

import javax.crypto.IllegalBlockSizeException;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ExecutionContext;
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

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ma.alakhdarbank.ccb.clients.ApiSendData;
import ma.alakhdarbank.ccb.exception.RCCBAppException;
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
	
	
	private String JSON_FILE_PATH="json.file.path";
	private String JSON_FILE_NAME="json.file.name";
	private String PASSWORD_HASH="password.hash";
	private String TOKEN="token";
	
	
	@Bean
	@StepScope
	/**
	 * from rest get token
	 * @return
	 */
    public ItemReader<String> jsonFileReader() {	
		
		return new SendDataStepReader(filepath, login, password);
 
    }
	
	
	
	
	@Bean
	@StepScope
	/**
	 * save token to flow or memrory 
	 * @return
	 */
    public ItemWriter<String> jsonFileWriter() {
		
		return new SendDataStepWriter(apiSendDataImp, login);
 
    }
	
	
	public class SendDataStepWriter implements ItemWriter<String>, ItemStream{
				
		private ApiSendData apiSendDataImp;
		
		private String login="";
		
		private String password_hash="";
		
		private String token="";
		
		public SendDataStepWriter(ApiSendData apiSendDataImp, String login) {
			super();
			this.apiSendDataImp = apiSendDataImp;
			this.login=login;
		}

		@Override
		public void write(List<? extends String> items) throws Exception {
			//ExecutionContext stepContext = this.stepExecution.getExecutionContext();
			//stepContext.put("auth_token", items.get(0)); 	
			HashMap<String, String> headers = new HashMap<String, String>();
			//TODO addinh headers
			headers.put("login", this.login);
			headers.put("password_hash",this.password_hash);
			headers.put("token",this.token);					
			
			apiSendDataImp.send((String)items.get(0), headers);
		}

		@Override
		public void open(ExecutionContext executionContext) throws ItemStreamException {
			this.password_hash=executionContext.getString(PASSWORD_HASH);
			this.token=executionContext.getString(TOKEN);
			
			
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
		
		private OutputStream outs;
		
		@Autowired
		private FileEncrypterDecrypter fileEncrypterDecrypterImp;
		
		@Autowired
		private SyncEncrypterDecrypter syncEncrypterDecrypterImp;
		
		@Autowired
		private RSAKeyManager rsaKeyManager;
		
		private String loginIn;
		
		private String passwordIn;
		
		private String pubCertPath;
		
		private String token;
		
		public SendDataStepReader(String path, String login, String password) {
			super();
			this.path = path;
			this.loginIn = login;
			this.passwordIn = password;			
		}
		
		
		private String encryptData (String spath) throws RCCBAppException, InvalidKeyException {
			Path path = Paths.get(spath);
			String filename= path.getFileName().toString();
			String dir = path.getParent().toAbsolutePath().toString();
			String newfilename = new StringBuilder().append(path).append(filename).append(".cyphering").toString();						
			fileEncrypterDecrypterImp.encrypt(path.toAbsolutePath().toString(),newfilename);
			try {
				byte[] key = syncEncrypterDecrypterImp.encrypt (fileEncrypterDecrypterImp.getSecretKey());
				this.token = syncEncrypterDecrypterImp.encodeData(key);
			} catch (InvalidKeyException | IllegalBlockSizeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return newfilename;
		}
		
		@BeforeStep
	    public void saveStepExecution(StepExecution stepExecution) throws RCCBAppException, InvalidKeyException {	        
	        JobParameters parameters = stepExecution.getJobExecution().getJobParameters();
	        this.path = parameters.getString(JSON_FILE_PATH);
	        this.filename = parameters.getString(JSON_FILE_NAME);
	        encryptData (path);
	        
	    }
		
		@Override
		public String read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
			String data = encryptFileAsString(path, filename);			 
			return syncEncrypterDecrypterImp.encodeData(data.getBytes());
		}

		public  String encryptFileAsString(String dir, String filename)throws Exception
	    {
			File out = new File (dir + filename + ".cypher");			
			this.outs =  fileEncrypterDecrypterImp.encrypt(dir + filename, out.getAbsolutePath());	
	        return new String(Files.readAllBytes(Paths.get(out.getAbsolutePath())));
	    }


		@Override
		public void open(ExecutionContext executionContext) throws ItemStreamException {
			this.path = executionContext.getString(JSON_FILE_PATH);
	        this.filename = executionContext.getString(JSON_FILE_NAME);	        
	        try {
				syncEncrypterDecrypterImp.init(pubCertPath);
			} catch (RCCBAppException e) {
				throw new ItemStreamException(e);
			}
		}


		@Override
		public void update(ExecutionContext executionContext) throws ItemStreamException {
			executionContext.put(TOKEN, token);
			
		}


		@Override
		public void close() throws ItemStreamException {
			try {
				if(outs!=null)outs.close();
			} catch (IOException e) {
				throw new ItemStreamException(e);
			}
			
		}
	
	}
}
