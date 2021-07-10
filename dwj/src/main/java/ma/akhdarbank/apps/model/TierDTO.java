package ma.akhdarbank.apps.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TierDTO {
	public String nom;
    public String prenom;
    public String raisonsociale;
    public String typepersonne;
    public String referencepiece;
    public String nationnalite;
    public String anneenaissance;
    public String mode;
    public Integer seuil;
    public Integer limit;
    public String typelist;
    
    
}
