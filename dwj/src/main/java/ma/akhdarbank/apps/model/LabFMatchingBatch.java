package ma.akhdarbank.apps.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;



import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "LAB_F_MATCHING_BATCH")
@Getter
@Setter
public class LabFMatchingBatch {
	
	@Column(name = "IDENTIFIANT")
	@Id
	private Long identifiant;
	
	@Column(name = "DATEJOURNEE")
	private String dateJournee;
	
	@Column(name = "NOMBRELIGNE")
	private String nombreLigne;
	
	@Column(name = "NUMTICKET")
	private String numTicket;
	
	@Column(name = "STATUT")
	private String statut;
	
	@Column(name = "MSGERREUR")
	private String msgerreur;
	
	
}
