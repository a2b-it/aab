/**
 * 
 */
package ma.alakhdarbank.ccb.persistence;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
	
	
	private Long getMaxLotAccepter () {
		return lotRepository.findMaxNLotByStatus(Lot.STATUT.ACCEPTER.getValue());
	}
	
	@Transactional
	public Lot getLastLotNotYetProceesed() {
		return lotRepository.findLastLotByStatus(Lot.STATUT.ENVOYER.getValue(), Lot.STATUT.ENCOURS .getValue());
	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public Lot saveNewLotENVOYER (String filename, int nbrEnr, Date dateArrete) {
		/*Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());		 
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
		lot.setDateArrete(cal.getTime());
		*/
		Lot lot = new Lot();
		Long newId = getMaxLotAccepter ();				
		lot.setIdLot((newId !=null)?++newId:1);
		lot.setNomfichier(filename);
		lot.setNbrCpt(nbrEnr);
		lot.setStatus(Lot.STATUT.SENDING.getValue());	
		lot.setDateArrete(dateArrete);
		lotRepository.save(lot);
		return lot;
	}
	
	@Transactional
	public Ctr saveNewCtrLot (Ctr ctr) throws RCCBAppException {		
		Ctr c = ctrRepository.save(ctr);
		updateLotStatus(Long.valueOf(ctr.getNlot ()), c);		
		return c;
	}
	
	@Transactional
	public Lot updateLotENVOYER (Long idLot, Date date) throws RCCBAppException {
		Optional<Lot> ol = lotRepository.findById(idLot);
		if(ol.isEmpty()) throw new RCCBAppException(" Lot Not found ["+idLot+"]");
		Lot lot = ol.get();
		
		lot.setStatus(STATUT.ENVOYER.getValue());
		lot.setDateEnvoi(date);				
		return lotRepository.save(lot);
	}
	
	@Transactional
	public Lot updateLotSENDIG (Long idLot, Date date) throws RCCBAppException {
		Optional<Lot> ol = lotRepository.findById(idLot);
		if(ol.isEmpty()) throw new RCCBAppException(" Lot Not found ["+idLot+"]");
		Lot lot = ol.get();
		
		lot.setStatus(STATUT.SENDING.getValue());
		lot.setDateEnvoi(date);		
		
		return lotRepository.save(lot);
	}
	
	private Lot updateLotStatus (Long idLot, Ctr ctr) throws RCCBAppException {
		Lot ol = lotRepository.findByIdLot(idLot);
		if(ol == null) throw new RCCBAppException(" Lot Not found ["+idLot+"]");
		if(ctr==null) throw new RCCBAppException(" Ctr Not found ["+idLot+"]");
		
		ol.setIdCtr(ctr.getId());
		if(ctr.getStatut() == CtrStatus.EN_ATTENTE) {
			ol.setStatus(STATUT.ENCOURS.getValue());
		}
		else if (ctr.getStatut() == CtrStatus.ACCEPTER_TOTAL || ctr.getStatut() == CtrStatus.ACCEPTER_W_ANO|| ctr.getStatut() == CtrStatus.REJET_PARTIEL) {
			ol.setStatus(STATUT.ACCEPTER.getValue());
		}else if (ctr.getStatut() == CtrStatus.REJET_TOTAL ) {
			ol.setStatus(STATUT.REJETER.getValue());
		}
		return lotRepository.save(ol);
	}
}
