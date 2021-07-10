package ma.akhdarbank.apps.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "LAB_F_MATCHING")
@Getter
@Setter
public class LabFMatching {
	
	@Column(name = "IDENTIFIANT")
	private Long identifiant;
	
	@Column(name = "ETXRIM")
	private String numRim;
	
	@Column(name = "NOM")
	private String nom;
	
	@Column(name = "PRENOM")
	private String prenom;
	
	@Column(name = "FIRSTNAME")
	private String firstname;
	
	@Column(name = "MIDDLENAME")
	private String middlename;
	
	@Column(name = "SURNAME")
	private String surname;
	
	@Column(name = "MAIDENNAME")
	private String maidenname;
	
	@Column(name = "REFPIECE")
	private String refpiece;
	
	@Column(name = "YEAR")
	private String year;
	
	@Column(name = "COUNTRY")
	private String country;
	
	@Column(name = "ADDRESS")
	private String address;
	
	@Column(name = "SCORE")
	private String score;
	
	@Column(name = "IDPERSONNE")
	private String idpersonne;
	
	@Column(name = "IDCLIENTCAM")
	private String idclientcam;
	
	@Column(name = "MSGERREUR")
	private String msgerreur;
	
	@Column(name = "DATEJOURNEE")
	private String datejournee;
	
	@Column(name = "ID_BATCH")
	private String idbatch;
	
	@Column(name = "RAISONSOCIAL")
	private String raisonsocial;
	
	
}
