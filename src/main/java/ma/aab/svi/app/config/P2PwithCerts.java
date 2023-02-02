/**
 * 
 */
package ma.aab.svi.app.config;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.security.crypto.codec.Hex;
import org.springframework.stereotype.Component;

import com.pcbsys.nirvana.client.nAbstractChannel;
import com.pcbsys.nirvana.client.nChannelAttributes;
import com.pcbsys.nirvana.client.nConsumeEvent;
import com.pcbsys.nirvana.client.nEventAttributes;
import com.pcbsys.nirvana.client.nEventProperties;
import com.pcbsys.nirvana.client.nQueue;
import com.pcbsys.nirvana.client.nQueueReaderContext;
import com.pcbsys.nirvana.client.nQueueSyncReader;
import com.pcbsys.nirvana.client.nQueueSyncTransactionReader;
import com.pcbsys.nirvana.client.nSession;
import com.pcbsys.nirvana.client.nSessionAttributes;
import com.pcbsys.nirvana.client.nSessionFactory;
import com.pcbsys.nirvana.client.nTransaction;
import com.pcbsys.nirvana.client.nTransactionAttributes;
import com.pcbsys.nirvana.client.nTransactionFactory;

import lombok.extern.slf4j.Slf4j;

/**
 * @author a.bouabidi
 *
 */
@Component
@Slf4j
public class P2PwithCerts {

	final String baseDir = "E:\\svi";
	final String RNAME = "nhp://172.16.11.50:9000/um1,nhp://172.16.11.50:9000/um2,nhp://172.16.11.50:9000/um3";
	final String USER = "365";
	final String PASSWORD = "75F6OL6osc";
	final String KEYSTOREPATH = this.baseDir + "\\ssl\\KS365.jks";
	final String KEYSTOREPASSWORD = "azerty";
	final String TRUSTSTOREPATH = this.baseDir + "\\ssl\\TS.jks";
	final String TRUSTSTOREPASSWORD = "azerty";
	final String QUEUE_IN = "VI_365_IN";
	final String QUEUE_OUT = "VI_365_OUT";
	final String QUEUE_ACK = "VI_365_ACK";
	final String QUEUE_REP = "VI_365_REP";
	final String TAG = "pacs.008";

	public void testconnection() throws Exception {
		log.debug("============================================ TEST CONNEXION =====================");
		DataInputStream dis = new DataInputStream(new FileInputStream(this.baseDir + "\\test.xml"));

		javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier(new javax.net.ssl.HostnameVerifier() {
			public boolean verify(String hosname, javax.net.ssl.SSLSession sslSession) {
				return true;
			}
		});
		try {
			nSession session = connect(
					"nhp://172.16.11.50:9000/um1,nhp://172.16.11.50:9000/um2,nhp://172.16.11.50:9000/um3",
					this.KEYSTOREPATH, KEYSTOREPASSWORD, this.TRUSTSTOREPATH, TRUSTSTOREPASSWORD, USER, PASSWORD);
			nQueue queueOut = getQueue(session, "VI_365_OUT");
			nQueue queueIn = getQueue(session, "VI_365_IN");
			send(dis, "pacs.008", queueIn);
			Thread.sleep(5000L);
			log.debug("============================================  RECEIVING MESSAGE  =====================");
			System.out.println(receive(queueOut));
			disconnect(session);
			log.debug("============================================  CONNEXION DISCONNECTED=====================");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private nQueue getQueue(nSession session, String queueName) throws Exception {
		nChannelAttributes cattrib = new nChannelAttributes();
		cattrib.setName(queueName);
		nQueue queue = session.findQueue(cattrib);
		System.out.println("Queue Retrieved: " + queue.getName());
		return queue;
	}

	public String getCheckSumHex(String message) {
		MessageDigest md = null;
		byte[] digest = null;
		try {
			md = MessageDigest.getInstance("SHA-256");

			digest = md.digest(message.getBytes(StandardCharsets.ISO_8859_1));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		String hex = String.valueOf(Hex.encode(digest));
		return hex;
	}

	private void send(DataInputStream dis, String tag, nAbstractChannel queue) throws Exception {
		byte[] datainBytes = new byte[dis.available()];
		dis.readFully(datainBytes);
		dis.close();

		String message = new String(datainBytes, 0, datainBytes.length);

		nTransactionAttributes tattrib = new nTransactionAttributes(queue);
		nTransaction myTransaction = nTransactionFactory.create(tattrib);
		nConsumeEvent evt = new nConsumeEvent(tag, message.getBytes());
		nEventAttributes evtAttrs = new nEventAttributes();
		evtAttrs.setMessageType((byte) 5);
		nEventProperties evtProperties = new nEventProperties();

		evtProperties.put("messageID", "5892672441367303195");
		evtProperties.put("messageName", tag);
		Long ums = Long.valueOf(evt.getTimestamp());
		Date date = new Date(ums.longValue());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy'-'MM'-'dd'T'HH':'mm':'ss'.'SSS");
		String localTimestamp = sdf.format(date);
		evtProperties.put("depositTimestamp", localTimestamp);
		String sha = getCheckSumHex(message);
		evtProperties.put("checksum", sha);

		evt.setAttributes(evtAttrs);
		evt.setProperties(evtProperties);

		myTransaction.publish(evt);
		myTransaction.commit();
	}

	private String receive(nQueue queue) throws Exception {
		nQueueSyncReader reader = queue.createTransactionalReader(new nQueueReaderContext());
		nConsumeEvent evt = reader.pop(1L);
		if (evt != null) {
			long id = evt.getEventID();
			String tag = evt.getEventTag();
			byte[] data = evt.getEventData();

			String message = new String(data, StandardCharsets.ISO_8859_1);
			((nQueueSyncTransactionReader) reader).commit(id);
			return "Message taggé " + tag + " contenant <" + message + ">";
		}
		return "Queue vide";
	}

	public nSession connect(String rnames, String keyStorePath, String keyStorePassword, String trustorePath,
			String truststorePassword, String myuser, String mypassword) throws Exception {
		nSessionAttributes sessionAttributes = new nSessionAttributes(rnames);
		String muser = new String(myuser);
		String mpassword = new String(mypassword);
		sessionAttributes.setKeystore(keyStorePath, keyStorePassword);
		sessionAttributes.setTruststore(trustorePath, truststorePassword);
		nSession connectionObject = nSessionFactory.create(sessionAttributes);
		connectionObject.init();
		return connectionObject;
	}

	private void disconnect(nSession session) {
		session.close();
		session = null;
	}
}
