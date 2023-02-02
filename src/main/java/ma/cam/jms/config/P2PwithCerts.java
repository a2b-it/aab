package ma.cam.jms.config;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
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



@Component
public class P2PwithCerts {
	
	
	//
	
	@Value("${aab.p2p.homedir}")
	private String HOME_DIR="C:\\Users\\bat00055\\Workspaces\\eclipse-workspace\\TestSVI";
	
	@Value("${aab.p2p.rname}")
	private String RNAME ="nhps://172.16.11.50:5555/ums1,nhps://172.16.11.50:5555/ums2,nhps://172.16.11.50:5555/ums3"; 
    		//"nhps://172.16.11.50:5555/ums1,nhps://172.16.11.50:5555/ums2,nhps://172.16.11.50:5555/ums3";
        
    // Ajouter le USER/PASSWORD pour s'authentifier
	@Value("${aab.p2p.user}")
	private String USER = "365";
	
	@Value("${aab.p2p.password}")
	private String PASSWORD = "75F6OL6osc";
  
    
	// Remplir les informations de KEYSTOREPATH chemin de la cle local
	// et le mot de pass
	@Value("${aab.p2p.keystorepath}")
	private String KEYSTOREPATH = HOME_DIR+"\\certs\\KS365.jks";
	
	@Value("${aab.p2p.keystorepassword}")
	private String KEYSTOREPASSWORD = "azerty";
	
	@Value("${aab.p2p.truststorepath}")
	private String TRUSTSTOREPATH = HOME_DIR+"\\certs\\TS.jks";
	
	@Value("${aab.p2p.truststorepassword}")
	private String TRUSTSTOREPASSWORD = "azerty";
	
	@Value("${aab.p2p.queue.in}")
	private String QUEUE_IN = "VI_365_IN";
	
	@Value("${aab.p2p.queue.out}")
	private String QUEUE_OUT = "VI_365_OUT";
	
	@Value("${aab.p2p.queue.ack}")
	private String QUEUE_ACK = "VI_365_ACK";
	
	@Value("${aab.p2p.queue.rep}")
	private String QUEUE_REP = "VI_365_REP";
	
	@Value("${aab.p2p.tag}")
	private String TAG = "pacs.008";

    /*
    public static void main(String[] args) throws Exception {
    	System.out.println("KEYSTOREPATH:"+KEYSTOREPATH);
    	System.out.println("TRUSTSTOREPATH:"+TRUSTSTOREPATH);
    	System.out.println("JAVA HOME:"+System.getProperty("java.home"));
        DataInputStream dis =  new DataInputStream (new FileInputStream (HOME_DIR+"\\in\\output.xml"));

        
        nSession session = connect(RNAME, KEYSTOREPATH, KEYSTOREPASSWORD,TRUSTSTOREPATH,TRUSTSTOREPASSWORD,USER,PASSWORD);      
        nQueue queueOut = getQueue(session, QUEUE_OUT);
        nQueue queueIn = getQueue(session, QUEUE_IN);
        send(dis, TAG, queueIn);
        Thread.sleep(5000);
        System.out.println(receive(queueOut));
        disconnect(session);
    }
*/
    
	public nSession createSession () throws Exception {
		 
	     nSession session = connect(RNAME, KEYSTOREPATH, KEYSTOREPASSWORD,TRUSTSTOREPATH,TRUSTSTOREPASSWORD,USER,PASSWORD);
	     return session;
	}
	
	private nSession connect2(String rnames, String keyStorePath, String keyStorePassword, String trustorePath, String truststorePassword, String myuser, String mypassword) throws Exception {
        nSessionAttributes sessionAttributes = new nSessionAttributes(rnames);
        String muser = new String(myuser);
        String mpassword = new String(mypassword);
        sessionAttributes.setKeystore(keyStorePath, keyStorePassword);
        sessionAttributes.setTruststore(trustorePath, truststorePassword);
        nSession connectionObject = nSessionFactory.create(sessionAttributes, muser, mpassword);
        connectionObject.init();
        return connectionObject;
    }
    
    public nQueue getQueue(nSession session, String queueName) throws Exception {
        nChannelAttributes cattrib = new nChannelAttributes();
        cattrib.setName(queueName);
        nQueue queue = session.findQueue(cattrib);
        System.out.println("Queue Retrieved: " + queue.getName());
        return queue;
    }

    public  String getCheckSumHex(String message){

        MessageDigest md = null;
        byte[] digest = null;

        try {
            md = MessageDigest.getInstance("SHA-256");
//            digest = md.digest(message.getBytes(StandardCharsets.UTF_8));
            digest = md.digest(message.getBytes(StandardCharsets.ISO_8859_1));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        String hex = String.valueOf(Hex.encode(digest));
        return hex;

    }
    
    public void send(DataInputStream dis, String tag, nAbstractChannel queue) throws Exception {

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
        Long ums = evt.getTimestamp();
        Date date = new Date(ums);
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


    public String receive(nQueue queue) throws Exception {
        nQueueSyncReader reader = queue.createTransactionalReader(new nQueueReaderContext());
        nConsumeEvent evt = reader.pop(1);
        if (evt != null) {
            long id = evt.getEventID();
            String tag = evt.getEventTag();
            byte[] data = evt.getEventData();
//            String message = new String(data, StandardCharsets.UTF_8);
			String message = new String(data, StandardCharsets.ISO_8859_1);
            ((nQueueSyncTransactionReader) reader).commit(id);
            return "Message tagged " + tag + " contenant <" + message + ">";

        }
        return "Queue vide";
    }

    /**
     * Connexion et obtention d'une session
     *
     * @param rnames
     * @return session
     * @throws Exception
     */
	private nSession connect(String rnames, String keyStorePath, String keyStorePassword, String trustorePath, String truststorePassword, String myuser, String mypassword) throws Exception {
		
		nSessionAttributes sessionAttributes = new nSessionAttributes(rnames);
	    
		sessionAttributes.setKeystore(keyStorePath, keyStorePassword);
		sessionAttributes.setTruststore(trustorePath, truststorePassword);
		nSession connectionObject = nSessionFactory.create(sessionAttributes, myuser,mypassword);
		connectionObject.init();
		return connectionObject;
	}

    /**
     * Liberation de la session
     *
     * @param session
     */
    void disconnect(nSession session) {
        session.close();
        session = null;
    }
}


