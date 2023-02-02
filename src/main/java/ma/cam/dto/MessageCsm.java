package ma.cam.dto;

public class MessageCsm {
	
	private Long identifiant;
	private String tag;
	private String fluxclob;
	private String messageid;
	
	
	public Long getIdentifiant() {
		return identifiant;
	}
	public void setIdentifiant(Long identifiant) {
		this.identifiant = identifiant;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public String getFluxclob() {
		return fluxclob;
	}
	public void setFluxclob(String fluxclob) {
		this.fluxclob = fluxclob;
	}
	public String getMessageid() {
		return messageid;
	}
	public void setMessageid(String messageid) {
		this.messageid = messageid;
	}

}
