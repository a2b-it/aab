package ma.akhdarbank.apps.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name = "EMPLOYEE")
public class Tiers {
	
	@Column(name = "IDENTIFIANT")
	private Long id;
	
	
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
}
