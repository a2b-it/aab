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

	@Query("select * from (select * from Lot l where l.status = ?1 order by l.dateEnvoi desc ) where rownum=1")
	Lot findLastLotByStatus(String status);
}
