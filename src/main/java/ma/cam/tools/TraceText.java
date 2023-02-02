package ma.cam.tools;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class TraceText {
	
	private List<ElemTrace> listeElemTrace;
	
	public TraceText() {
		if (this.listeElemTrace ==null ) {
			listeElemTrace = new ArrayList<TraceText.ElemTrace>();
		}
	}
	
	public List<ElemTrace> getListeElemTrace() {
		return listeElemTrace;
	}

	public void setListeElemTrace(List<ElemTrace> listeElemTrace) {
		this.listeElemTrace = listeElemTrace;
	}

	public void addTraceElement(String message,String typemessage) {
		listeElemTrace.add(new ElemTrace(message,typemessage));
	}
	
	@Override
	public String toString() {
		String returnedText="";
		if (this.listeElemTrace !=null && !this.listeElemTrace.isEmpty()) {
			for (ElemTrace elemTrace : listeElemTrace) {
				returnedText+="<br /><span style=\"color:"+ (elemTrace.getTypemessage().equalsIgnoreCase("R")?"RED":"GREEN") +"\">"+elemTrace.message+"</span>"  ;
			}
		}			
		return returnedText;
	}
	
	public class ElemTrace{
		private String message;
		private String typemessage;//R - B
		
		public ElemTrace(String message,String typemessage) {
			this.message = message;
			this.typemessage = typemessage;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		public String getTypemessage() {
			return typemessage;
		}

		public void setTypemessage(String typemessage) {
			this.typemessage = typemessage;
		}
	}
}
