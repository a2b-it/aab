/**
 * 
 */
package ma.alakhdarbank.ccb.persistence;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ma.alakhdarbank.ccb.entity.Ctr;
import ma.alakhdarbank.ccb.entity.CtrStatus;
import ma.alakhdarbank.ccb.entity.Lot;
import ma.alakhdarbank.ccb.entity.Lot.STATUT;
import ma.alakhdarbank.ccb.exception.RCCBAppException;

/**
 * @author a.bouabidi
 *
 */
@Service
public class ServiceLot {
	
	@Autowired
	private LotRepository lotRepository;
	
	@Autowired
	private CtrRepository ctrRepository;
	
	private Integer getMaxLotAccepter () {
		return lotRepository.findMaxIdByStatus(Lot.STATUT.ACCEPTER.toString());
	}
	
	public Lot getLastLotEnvoyer () {
		return lotRepository.findLastLotByStatus(Lot.STATUT.ENVOYER.toString());
	}
	
	public Lot saveNewLotENVOYER (String filename, int nbrEnr, Date dateArrete) {
		/*Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());		 
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
		lot.setDateArrete(cal.getTime());
		*/
		Lot lot = new Lot();
		Integer newId = getMaxLotAccepter ();				
		lot.setIdLot(newId++);
		lot.setNomfichier(filename);
		lot.setNbrCpt(nbrEnr);
		lot.setStatus(Lot.STATUT.SENDING);	
		lot.setDateArrete(dateArrete);
		lotRepository.save(lot);
		return lot;
	}
	
	
	public Ctr saveNewCtrLot (Ctr lot) throws RCCBAppException {		
		Ctr c = ctrRepository.save(lot);
		updateLotStatus(lot.getNlot(), lot);		
		return c;
	}
	
	public Lot updateLotENVOYER (Integer idLot, Date date) throws RCCBAppException {
		Optional<Lot> ol = lotRepository.findById(idLot);
		if(ol.isEmpty()) throw new RCCBAppException(" Lot Not found ["+idLot+"]");
		Lot lot = ol.get();
		
		lot.setStatus(STATUT.ENVOYER);
		lot.setDateEnvoi(date);		
		
		return lotRepository.save(lot);
	}
	
	public Lot updateLotStatus (Integer idLot, Ctr ctr) throws RCCBAppException {
		Optional<Lot> ol = lotRepository.findById(idLot);
		if(ol.isEmpty()) throw new RCCBAppException(" Lot Not found ["+idLot+"]");
		Lot lot = ol.get();
		if (ctr.getStatut() == CtrStatus.ACCEPTER_TOTAL || ctr.getStatut() == CtrStatus.ACCEPTER_W_ANO) {
			lot.setStatus(STATUT.ACCEPTER);
		}else if (ctr.getStatut() == CtrStatus.REJET_TOTAL || ctr.getStatut() == CtrStatus.REJET_PARTIEL) {
			lot.setStatus(STATUT.REJETER);
		}
		return lotRepository.save(lot);
	}
}
