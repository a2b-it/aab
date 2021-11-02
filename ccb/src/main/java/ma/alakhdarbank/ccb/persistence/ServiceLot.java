/**
 * 
 */
package ma.alakhdarbank.ccb.persistence;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ma.alakhdarbank.ccb.entity.Lot;

/**
 * @author a.bouabidi
 *
 */
@Service
public class ServiceLot {
	
	@Autowired
	private LotRepository lotRepository;
	
	
	private Integer getMaxLot () {
		return lotRepository.findMaxIdByStatus(Lot.STATUT.ACCEPTER.toString());
	}
	
	public void set_newLotENVOYER (String filename) {
		Lot lot = new Lot();
		Integer newId = getMaxLot ();		
		lot.setDateenvoi(new Date());
		lot.setIdLot(newId++);
		lot.setNomfichier(filename);
		lot.setStatus(Lot.STATUT.ENVOYER);		
		lotRepository.save(lot);
	}
}
