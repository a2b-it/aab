package ma.akhdarbank.apps.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "Tiers")
@Getter
@Setter
public class Tiers {
	
	@Column(name = "IDENTIFIANT")
	@Id
	private Long id;
	
	
	public Tiers(Long id, String typepersonne, String nom, String prenom, 
			String raisonsocial, String numrim) {
		super();
		this.id = id;
		this.typepersonne = typepersonne;
		this.nom = nom;
		this.prenom = prenom;
		this.raisonsocial = raisonsocial;
		this.numrim = numrim;
	}
	
	
	@Column(name = "TYPEPERSONNE")
	private String typepersonne;
	
	@Column(name = "NOM")
	private String nom;
	
	@Column(name = "PRENOM")
	private String prenom;
	
	@Column(name = "RAISONSOCIALE")
	private String raisonsocial;
	
	@Column(name = "ETX_RIM")
	private String numrim;
	
	@Column(name = "ID_BATCH")
	private String idbatch;
	
	@Column(name = "ID_PERSONNE")
	private String idpersonne;
	
	@Column(name = "ANNEENAISSANCE")
	private String anneenaissance;
	
	@Column(name = "NATIONALITE")
	private String nationnalite;
	
	
	@Column(name = "REFERENCEPIECE")
	private String referencepiece;
	
	public TierBatch.TierBatchReq toDTOObject() {
		
    	return new TierBatch.TierBatchReq(getNom(),getPrenom(), 
    			getRaisonsocial(),getTypepersonne(),
    			getReferencepiece(),getNationnalite(),getAnneenaissance(),getNumrim() 
    			);
    }

	public Tiers(Long id, String typepersonne, String nom, String prenom, String raisonsocial, String numrim,
			String idbatch, String idpersonne, String anneenaissance, String nationnalite, String referencepiece) {
		super();
		this.id = id;
		this.typepersonne = typepersonne;
		this.nom = nom;
		this.prenom = prenom;
		this.raisonsocial = raisonsocial;
		this.numrim = numrim;
		this.idbatch = idbatch;
		this.idpersonne = idpersonne;
		this.anneenaissance = anneenaissance;
		this.nationnalite = nationnalite;
		this.referencepiece = referencepiece;
	}
}
