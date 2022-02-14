package ma.akhdarbank.apps.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


public class TierBatch {
	
	@AllArgsConstructor
	@Getter
	@Setter
	public static class TierBatchReq{		
		public String nom;
	    public String prenom;
	    public String raisonsociale;
	    public String typepersonne;
	    public String referencepiece;
	    public String nationnalite;
	    public String anneenaissance;
	    public String idclient;
	}
	
	@AllArgsConstructor
	@Getter
	@Setter
	public static class TierBatchRep{
		
		public TierBatchRep() {
			super();	
		}
		public String firstname;
	    public String middlename;
	    public String surname;
	    public String maidenname;
	    public String refpiece;
	    public String year;
	    public String country;
	    public String address;
	    public String score;
	    public String idpersonne;
	    public String idclient;
	    public String msgerreur;
	    public String reference;
	    public boolean flagun;
	    public boolean flague;
	    public boolean flagus;
	    public boolean flagpep;
	    public String raisonsocial;
	}
	

}
