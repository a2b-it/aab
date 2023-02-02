package ma.cam.dto;

public class ParamsQueue {
	private String code;
	private String nom;
	private String inout;
	private String codequeuescpi;
	private String flaglistener;
	private String nomprocedure;
	private String statut;
	
	
	public ParamsQueue() {
		
	}
	
	
	
	public ParamsQueue(String code, String nom, String inout, String codequeuescpi, String flaglistener,
			String nomprocedure, String statut) {
		super();
		this.code = code;
		this.nom = nom;
		this.inout = inout;
		this.codequeuescpi = codequeuescpi;
		this.flaglistener = flaglistener;
		this.nomprocedure = nomprocedure;
		this.statut = statut;
	}



	public String getCode() {
		return code;
	}



	public void setCode(String code) {
		this.code = code;
	}



	public String getNom() {
		return nom;
	}



	public void setNom(String nom) {
		this.nom = nom;
	}



	public String getInout() {
		return inout;
	}



	public void setInout(String inout) {
		this.inout = inout;
	}



	public String getCodequeuescpi() {
		return codequeuescpi;
	}



	public void setCodequeuescpi(String codequeuescpi) {
		this.codequeuescpi = codequeuescpi;
	}



	public String getFlaglistener() {
		return flaglistener;
	}



	public void setFlaglistener(String flaglistener) {
		this.flaglistener = flaglistener;
	}



	public String getNomprocedure() {
		return nomprocedure;
	}



	public void setNomprocedure(String nomprocedure) {
		this.nomprocedure = nomprocedure;
	}



	public String getStatut() {
		return statut;
	}



	public void setStatut(String statut) {
		this.statut = statut;
	}

}
