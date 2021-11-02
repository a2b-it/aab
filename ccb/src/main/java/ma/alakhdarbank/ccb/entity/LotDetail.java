/**
 * 
 */
package ma.alakhdarbank.ccb.entity;

/**
 * @author a.bouabidi
 *
 */
public class LotDetail {
	
	private Integer idLot;
	
	private STATUT statut;
	
	public enum STATUT {
		A,
		E,
		Z,
		L
	}

}
