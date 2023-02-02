package ma.cam.jms.handler.message.nirvana;
import com.pcbsys.nirvana.client.nConsumeEvent;

public class ConsumeEventWrapper {
	final nConsumeEvent event;
	public ConsumeEventWrapper(nConsumeEvent event){
		this.event = event;
	}
}
