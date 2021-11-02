/**
 * 
 */
package ma.alakhdarbank.ccb.persistence;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ma.alakhdarbank.ccb.entity.Lot;

/**
 * @author a.bouabidi
 *
 */
@Repository
public interface LotRepository extends CrudRepository<Lot, Integer>{
	
	@Query("select max(l.idLot) from Lot l where l.status = ?1")
	Integer findMaxIdByStatus(String status);

}
