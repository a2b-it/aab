/**
 * 
 */
package ma.alakhdarbank.ccb;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ma.alakhdarbank.ccb.clients.ApiSendData;
import ma.alakhdarbank.ccb.entity.Lot;
import ma.alakhdarbank.ccb.exception.RCCBAppException;
import ma.alakhdarbank.ccb.persistence.ServiceLot;
import ma.alakhdarbank.ccb.sec.FileEncrypterDecrypter;
import ma.alakhdarbank.ccb.sec.RSAKeyManager;
import ma.alakhdarbank.ccb.sec.SyncEncrypterDecrypter;
import ma.alakhdarbank.dto.Clis;
import ma.alakhdarbank.dto.Cpt;

/**
 * @author a.bouabidi
 *
 */
@Component
@NoArgsConstructor
@Getter
@Setter
@Slf4j
public class SendDataStepRWP {

	@Autowired
	private ApiSendData apiSendDataImp;

	@Value("${bkam.sendCCBData.filepath.url}")
	public String filepath;

	@Value("${bkam.public_key.path}")
	public String publicKeyPath;

	@Value("${bkam.workfile.path}")
	public String workfilePath;

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

	public String JSON_FILE_PATH = "json.file.path";
	public String JSON_FILE_NAME = "json.file.name";
	public String PUBLIC_KEY_PATH = "public_key.path";

	public String PASSWORD_HASH = "password.hash";
	public String TOKEN = "token";
	public String LOGIN = "login";
	public String PUB_CERT_PATH = "pub.cert.path";

	public String ID_LOT = "id.lot";
	public String ID = "id";
	public String NBR_CPT = "nbr.cpt";
	public String FILENAME = "filename";
	public String DATEARRETE = "datearrete";

	@Autowired
	private ApplicationContext context;

	@Bean

	/**
	 * from rest get token
	 * 
	 * @return
	 */
	public ItemReader<String> jsonFileReader() {

		ServiceLot serviceLot = context.getBean(ServiceLot.class);
		return new SendDataStepReader(serviceLot);

	}

	@Bean

	/**
	 * from rest get token
	 * 
	 * @return
	 */
	public ItemProcessor<String, String> jsonFileProcessor() {
		return new SendDataStepProcessor(fileEncrypterDecrypterImp, syncEncrypterDecrypterImp, rsaKeyManager);

	}

	@Bean

	/**
	 * save token to flow or memrory
	 * 
	 * @return
	 */
	public ItemWriter<String> jsonFileWriter() {
		ServiceLot serviceLot = context.getBean(ServiceLot.class);
		return new SendDataStepWriter(apiSendDataImp, serviceLot);

	}

	@Getter
	@Setter
	public class SendDataStepProcessor implements ItemProcessor<String, String>, ItemStream {

		private FileEncrypterDecrypter fileEncrypterDecrypterImp;

		private SyncEncrypterDecrypter syncEncrypterDecrypterImp;

		private RSAKeyManager rsaKeyManager;

		private String tokenValue;

		private ExecutionContext executionContext;

		/*
		 * @AfterStep public void afterStep(StepExecution stepExecution) throws
		 * RCCBAppException { stepExecution.getExecutionContext().put(TOKEN,
		 * this.token); }
		 */

		@Override
		public String process(String item) throws Exception {
			String r = encryptData(item);
			writeTempFile(item, r);
			executionContext.putString(TOKEN, getTokenValue());
			return r;

		}

		private void writeTempFile(String content, String crypted) throws IOException {
			String filename = executionContext.getString(FILENAME);
			Path tempToken = Files
					.createFile(Paths.get(workfilePath, filename.substring(0, filename.length() - 5) + ".token"));
			Files.write(tempToken, getTokenValue().getBytes(StandardCharsets.UTF_8));

			Path tempFile = Files
					.createFile(Paths.get(workfilePath, filename.substring(0, filename.length() - 5) + ".bin"));
			Files.write(tempFile, crypted.getBytes(StandardCharsets.UTF_8));

		}

		public SendDataStepProcessor(FileEncrypterDecrypter fileEncrypterDecrypterImp,
				SyncEncrypterDecrypter syncEncrypterDecrypterImp, RSAKeyManager rsaKeyManager) {
			super();
			this.fileEncrypterDecrypterImp = fileEncrypterDecrypterImp;
			this.syncEncrypterDecrypterImp = syncEncrypterDecrypterImp;
			this.rsaKeyManager = rsaKeyManager;
		}

		private String encryptData(String spath) throws RCCBAppException, InvalidKeyException {
			byte[] ciphered = fileEncrypterDecrypterImp.encrypt(spath);
			try {
				byte[] key = syncEncrypterDecrypterImp.encrypt(fileEncrypterDecrypterImp.getSecretKey());
				this.tokenValue = syncEncrypterDecrypterImp.encodeData(key);
			} catch (InvalidKeyException | IllegalBlockSizeException e) {
				throw new RCCBAppException(e);
			}
			return syncEncrypterDecrypterImp.encodeData(ciphered);
		}

		@Override
		public void open(ExecutionContext executionContext) throws ItemStreamException {
			this.executionContext = executionContext;
			// JobParameters parameters =
			// stepExecution.getJobExecution().getJobParameters();
			System.out.println("================== open processor");
			String public_secrt = executionContext.getString(PUB_CERT_PATH);
			// TODO throw exception if public_secrt is null
			try {
				syncEncrypterDecrypterImp.init(public_secrt);
				fileEncrypterDecrypterImp.init();
			} catch (RCCBAppException | InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException
					| InvalidAlgorithmParameterException e1) {
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
	public class SendDataStepWriter implements ItemWriter<String>, ItemStream {

		private ApiSendData apiSendDataImp;

		private ServiceLot serviceLot;

		private ExecutionContext executionContext;

		public SendDataStepWriter(ApiSendData apiSendDataImp, ServiceLot serviceLot) {
			super();
			this.apiSendDataImp = apiSendDataImp;
			this.serviceLot = serviceLot;

		}
		/*
		 * @BeforeStep public void saveStepExecution(StepExecution stepExecution) {
		 * JobParameters parameters =
		 * stepExecution.getJobExecution().getJobParameters(); this.login =
		 * parameters.getString(LOGIN); this.password_hash =
		 * parameters.getString(PASSWORD); this.token = parameters.getString(TOKEN);
		 * this.idLot = parameters.getLong(ID_LOT); this.nbrCpt=
		 * parameters.getLong(NBR_CPT).intValue(); }
		 */

		@Override
		public void write(List<? extends String> items) {
			log.debug("================== write");
			// ExecutionContext stepContext = this.stepExecution.getExecutionContext();
			// stepContext.put("auth_token", items.get(0));
			HashMap<String, String> headers = new HashMap<String, String>();
			// TODO adding headers
			Long idLot = executionContext.getLong(ID_LOT);
			Long id = executionContext.getLong(ID);
			String token = executionContext.getString(TOKEN);
			Long nbrCpt = executionContext.getLong(NBR_CPT);
			String filename = executionContext.getString(FILENAME);
			try {
				SimpleDateFormat fa = new SimpleDateFormat("yyyyMMdd");
				Date datearrete = fa.parse(executionContext.getString(DATEARRETE));
				SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");

				headers.put("serviceBAM", "CCB");
				headers.put("idLot", idLot.toString());
				headers.put("emetteur", "365");
				headers.put("recepteur", "001");
				headers.put("dateDeclaration", f.format(datearrete));
				headers.put("nbrEnregistrement", String.valueOf(nbrCpt));
				headers.put("login", getLogin());
				headers.put("password_hash", getPassword());
				headers.put("token", token);
				// headers.put("Content-Type","application/octet-stream");
				apiSendDataImp.send((String) items.get(0), headers);
				serviceLot.updateLotENVOYER(id, new Date());
				//
				// Archive file after processing
				File file = Paths.get(getWorkfilePath(), filename).toFile();
				try {
					Files.move(Paths.get(file.getParent(), file.getName()),
							Paths.get(file.getParent(), file.getName() + ".archived"));
					Files.move(Paths.get(file.getParent(), file.getName().replace(".json", ".bin")),
							Paths.get(file.getParent(), file.getName().replace(".json", ".bin") + ".archived"));
					Files.move(Paths.get(file.getParent(), file.getName().replace(".json", ".token")),
							Paths.get(file.getParent(), file.getName().replace(".json", ".token") + ".archived"));
				} catch (IOException e) {
					log.error(e.getMessage(), e);
				}
			} catch (RCCBAppException | ParseException e) {// make all exception silent
				log.error(e.getMessage(), e);

			} finally {

			}
			//

		}

		@Override
		public void open(ExecutionContext executionContext) throws ItemStreamException {
			// JobParameters parameters = executionContext.get
			this.executionContext = executionContext;

			System.out.println("================== open writer");
			// this.login =
			// (executionContext.containsKey(LOGIN))?executionContext.getString(LOGIN):null;
			// this.password_hash =(executionContext.containsKey(PASSWORD_HASH))?
			// executionContext.getString(PASSWORD_HASH):null;
			/*
			 * this.token = (executionContext.containsKey(TOKEN))?
			 * executionContext.getString(TOKEN):null;
			 * 
			 * 
			 * this.idLot = (executionContext.containsKey(ID_LOT))?
			 * executionContext.getLong(ID_LOT):null; this.nbrCpt=
			 * (executionContext.containsKey(NBR_CPT))?executionContext.getLong(NBR_CPT):
			 * null;
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
	public class SendDataStepReader implements ItemReader<String> {

		private String path;

		private String wpath;

		private String filename;

		private String publicCert;

		private int j = 0;

		private ServiceLot serviceLot;

		private int MaxCpt = 6000;

		private ExecutionContext executionContext;

		private File[] jsons;

		public SendDataStepReader(ServiceLot serviceLot) {
			super();
			this.serviceLot = serviceLot;

		}

		@BeforeStep
		public void saveStepExecution(StepExecution stepExecution) throws RCCBAppException, InvalidKeyException {
			JobParameters parameters = stepExecution.getJobExecution().getJobParameters();
			this.path = getFilepath();
			this.wpath = getWorkfilePath();
			// create working dir
			Paths.get(wpath).toFile().mkdirs();
			//
			this.filename = getFilename();
			this.publicCert = getPublicKeyPath();

			// save variable to context
			this.executionContext = stepExecution.getExecutionContext();
			this.executionContext.putString(PUB_CERT_PATH, getPublicCert());
			//
			try {
				splitFile();
			} catch (IOException e) {
				throw new RCCBAppException(e);
			}
		}

		private void splitFile() throws IOException {
			ma.alakhdarbank.dto.Lot lot = null;
			ObjectMapper om = new ObjectMapper();
			om.setSerializationInclusion(Include.NON_NULL);
			File[] files = new File(path).listFiles(new FilenameFilter() {
				@Override
				public boolean accept(File dir, String name) {
					return name.toLowerCase().endsWith(".json");
				}
			});
			for (File file : files) {
				int i = 0;
				String tempFile = readFileAsString(file.getParent(), file.getName());
				ma.alakhdarbank.dto.Lot glob = om.readValue(tempFile, ma.alakhdarbank.dto.Lot.class);
				while (i != glob.cpts.size()) {
					lot = extract(glob, i);
					if (lot.cpts.size() < MaxCpt) {
						i = glob.cpts.size();
					} else {
						i = i + MaxCpt;
					}

					long id = new Date().getTime();
					String newContent = om.writeValueAsString(lot);
					Path newJson = Files.createFile(Paths.get(workfilePath, file.getName()+"_"+String.valueOf(id) + ".json"));
					Files.write(newJson, newContent.getBytes(StandardCharsets.UTF_8));
				}
				// archive file after processing
				Files.move(Paths.get(file.getParent(), file.getName()),
						Paths.get(file.getParent(), file.getName() + ".archived"));
			}

		}

		private ma.alakhdarbank.dto.Lot extract(ma.alakhdarbank.dto.Lot lot, int index) {
			if (lot.cpts.size() < MaxCpt) {
				lot.h07 = String.valueOf(lot.cpts.size());
				lot.h08 = String.valueOf(calculerNombreClient(lot.cpts));
				lot.h09 = lot.h07;
				return lot;
			} else {
				if (index + MaxCpt < lot.cpts.size()) {
					ma.alakhdarbank.dto.Lot loti = new ma.alakhdarbank.dto.Lot();
					loti.h01 = lot.h01;
					loti.h02 = lot.h02;
					loti.h03 = lot.h03;
					loti.h04 = lot.h04;
					loti.h05 = lot.h05;
					loti.h06 = lot.h06;
					loti.cpts = lot.cpts.subList(index, index + MaxCpt);
					loti.h07 = String.valueOf(loti.cpts.size());
					loti.h08 = String.valueOf(calculerNombreClient(loti.cpts));
					loti.h09 = loti.h07;
					return loti;
				} else {
					ma.alakhdarbank.dto.Lot loti = new ma.alakhdarbank.dto.Lot();
					loti.h01 = lot.h01;
					loti.h02 = lot.h02;
					loti.h03 = lot.h03;
					loti.h04 = lot.h04;
					loti.h05 = lot.h05;
					loti.h06 = lot.h06;
					loti.h07 = lot.h07;
					loti.cpts = lot.cpts.subList(index, lot.cpts.size());
					loti.h07 = String.valueOf(loti.cpts.size());
					loti.h08 = String.valueOf(calculerNombreClient(loti.cpts));
					loti.h09 = loti.h07;
					return loti;
				}
			}
		}

		private int calculerNombreClient(List<Cpt> cpts) {
			int total = 0;
			ArrayList<Clis> clients = new ArrayList<Clis>();
			for (Cpt cpt : cpts) {
				clients.addAll(cpt.getClis());
			}
			Map<String, Long> countForId = clients.stream()
					.collect(Collectors.groupingBy(Clis::getId, Collectors.counting()));

			return countForId.size();

		}

		/**
		 * no more than one file is processed
		 * 
		 * @throws RCCBAppException
		 * @throws java.text.ParseException
		 */

		@Override
		public String read() throws RCCBAppException, java.text.ParseException {
			System.out.println("================== read .json files");
			SimpleDateFormat f = new SimpleDateFormat("yyyyMMdd");
			ObjectMapper om = new ObjectMapper();
			om.setSerializationInclusion(Include.NON_NULL);
			ma.alakhdarbank.dto.Lot lot = null;
			//
			Lot l = this.serviceLot.getLastLotNotYetProceesed();
			if (l != null) {
				// a file is pending
				return null;
			}
			//
			if (this.jsons == null) {
				this.jsons = new File(wpath).listFiles(new FilenameFilter() {
					@Override
					public boolean accept(File dir, String name) {
						return name.toLowerCase().endsWith(".json");
					}
				});
			}
			if (this.j >= jsons.length) {
				this.j = 0;
				this.jsons = null;
				// Files.move(Paths.get(path), Paths.get(path,"archive"));
				return null;
			}

			File currentFile = jsons[this.j++];
			this.filename = currentFile.getName();
			String content;
			try {
				content = readFileAsString2(currentFile.getParent(), currentFile.getName());
				lot = om.readValue(content, ma.alakhdarbank.dto.Lot.class);
			} catch (IOException e) {
				throw new RCCBAppException(e);
			}

			// one file is processed

			int nbrEnr = Integer.valueOf(lot.cpts.size());
			Date dateArrete = f.parse(lot.h03);
			Lot savedLot = this.serviceLot.saveNewLotENVOYER(filename, nbrEnr, dateArrete);

			//
			executionContext.putLong(ID_LOT, savedLot.getIdLot());
			executionContext.putLong(ID, savedLot.getId());
			executionContext.putLong(NBR_CPT, savedLot.getNbrCpt());
			executionContext.putString(FILENAME, filename);
			executionContext.putString(DATEARRETE, lot.h03);

			//
			int posd = content.indexOf("\"h05\"");
			int posf = content.indexOf("\"h06\"");
			String debut = content.substring(0, posd + 5);
			String fin = content.substring(posf);
			StringBuilder b = new StringBuilder().append(debut).append(":\"").append(savedLot.getIdLot()).append("\",")
					.append(fin);
			return b.toString();
		}

		public String readFileAsString(String dir, String filename) throws IOException {
			FileInputStream fis = null;
			String content = null;
			try {
				fis = new FileInputStream(Paths.get(dir, filename).toString());
				InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
				BufferedReader buffReader = new BufferedReader(isr);
				StringBuilder sb = new StringBuilder();
				String line;
				while ((line = buffReader.readLine()) != null) {
					sb.append(line);
				}
				/*
				 * byte[] buffer = new byte[10000]; StringBuilder sb = new
				 * StringBuilder(); while (fis.read(buffer) != -1) { sb.append(new
				 * String(buffer)); buffer = new byte[10000]; }
				 */
				content = sb.toString();
			} catch (FileNotFoundException e) {
				throw e;
			} catch (IOException e) {
				throw e;
			} finally {
				try {
					fis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return content;
		}

		public String readFileAsString2(String dir, String filename) throws IOException {

			return new String(Files.readAllBytes(Paths.get(dir, filename)), StandardCharsets.UTF_8);
		}

	}
}
