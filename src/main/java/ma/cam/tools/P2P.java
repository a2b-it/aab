package ma.cam.tools;

import com.pcbsys.nirvana.client.nAbstractChannel;
import com.pcbsys.nirvana.client.nChannelAttributes;
import com.pcbsys.nirvana.client.nChannelNotFoundException;
import com.pcbsys.nirvana.client.nConsumeEvent;
import com.pcbsys.nirvana.client.nEventAttributes;
import com.pcbsys.nirvana.client.nIllegalArgumentException;
import com.pcbsys.nirvana.client.nQueue;
import com.pcbsys.nirvana.client.nQueueReaderContext;
import com.pcbsys.nirvana.client.nQueueSyncReader;
import com.pcbsys.nirvana.client.nQueueSyncTransactionReader;
import com.pcbsys.nirvana.client.nRealmUnreachableException;
import com.pcbsys.nirvana.client.nSecurityException;
import com.pcbsys.nirvana.client.nSession;
import com.pcbsys.nirvana.client.nSessionAlreadyInitialisedException;
import com.pcbsys.nirvana.client.nSessionAttributes;
import com.pcbsys.nirvana.client.nSessionFactory;
import com.pcbsys.nirvana.client.nSessionNotConnectedException;
import com.pcbsys.nirvana.client.nTransaction;
import com.pcbsys.nirvana.client.nTransactionAttributes;
import com.pcbsys.nirvana.client.nTransactionFactory;
import java.nio.charset.StandardCharsets;

import org.springframework.stereotype.Service;

@Service
public class P2P {
	private String rname ;
	private String queue_in; // mock
	private String queue_out;
	private String tag;

//	public static void main(String[] args) throws Exception {
//		nSession session = connect(RNAME);
//		nQueue queueOut = getQueue(session, QUEUE_OUT);
//		nQueue queueIn = getQueue(session, QUEUE_OUT);
//		send("Contenu du message SWIFT PACS008", TAG, queueOut);
//		System.out.println(receive(queueIn));
//		disconnect(session);
//	}

	/**
	 * Recupere une instance de connexion de QUEUE
	 *
	 * @param Session
	 * @param NomDeLaQueue
	 * @return Queue
	 * @throws Exception
	 */
	public nQueue getQueue(nSession session, String queueName) throws Exception {
		nChannelAttributes cattrib = new nChannelAttributes();
		cattrib.setName(queueName);
		nQueue queue = session.findQueue(cattrib);
		System.out.println("Queue Retrieved: " + queue.getName());
		return queue;
	}

	public String getRname() {
		return rname;
	}

	public void setRname(String rname) {
		this.rname = rname;
	}

	public String getQueue_in() {
		return queue_in;
	}

	public void setQueue_in(String queue_in) {
		this.queue_in = queue_in;
	}

	public String getQueue_out() {
		return queue_out;
	}

	public void setQueue_out(String queue_out) {
		this.queue_out = queue_out;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	/**
	 * Envoie le message accompagné de son tag (ISO 20022) vers la queue
	 *
	 * @param Message
	 * @param Tag
	 * @param Queue   destinataire
	 * @throws Exception
	 */
	public void send(String message, String tag, nAbstractChannel queue) throws Exception {
		nTransactionAttributes tattrib = new nTransactionAttributes(queue);
		nTransaction myTransaction = nTransactionFactory.create(tattrib);
		nConsumeEvent evt = new nConsumeEvent(tag, message.getBytes());
		nEventAttributes evtAttrs = new nEventAttributes();
		evtAttrs.setMessageType((byte) 5);
		evt.setAttributes(evtAttrs);
		myTransaction.publish(evt);
		myTransaction.commit();
	}

	/**
	 * Recupere le message posté par l'expediteur
	 *
	 * @param Queue
	 * @throws Exception
	 */
	public nConsumeEvent receive(nQueue queue) throws Exception {
		nQueueSyncReader reader = queue.createTransactionalReader(new nQueueReaderContext());
		nConsumeEvent evt = reader.pop(1);
		
		if (evt != null) {
			long id = evt.getEventID();
			((nQueueSyncTransactionReader) reader).commit(id);
			return evt;
		}
		
		throw new Exception("Queue vide");
	}

	/**
	 * Connexion et obtention d'une session
	 *
	 * @param rnames
	 * @return session
	 * @throws nIllegalArgumentException 
	 * @throws nSessionAlreadyInitialisedException 
	 * @throws nSessionNotConnectedException 
	 * @throws nSecurityException 
	 * @throws Exception
	 */
	public nSession connect(String rnames) throws nRealmUnreachableException, Exception {
		nSessionAttributes sessionAttributes = new nSessionAttributes(rnames);
		nSession connectionObject = nSessionFactory.create(sessionAttributes);
		connectionObject.init();
		return connectionObject;
	}

	/**
	 * Liberation de la session
	 *
	 * @param session
	 */
	public void disconnect(nSession session) {
		session.close();
		session = null;
	}
}
