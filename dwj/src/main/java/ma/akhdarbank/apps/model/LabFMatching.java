package ma.akhdarbank.apps.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;



import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "LAB_F_MATCHING")
@Getter
@Setter
public class LabFMatching {
	
	@Column(name = "IDENTIFIANT")
	@Id
	private Long identifiant;
	
	@Column(name = "ETX_RIM")
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
	private Double score;
	
	@Column(name = "IDPERSONNE")
	private String idpersonne;
	
	@Column(name = "IDCLIENTCAM")
	private String idclientcam;
	
	@Column(name = "MSGERREUR")
	private String msgerreur;
	
	@Column(name = "DATEJOURNEE")
	private Date datejournee;
	
	@Column(name = "ID_BATCH")
	private Long idbatch;
	
	@Column(name = "RAISONSOCIAL")
	private String raisonsocial;
	
	
}
