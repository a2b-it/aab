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
public interface LotRepository extends CrudRepository<Lot, Long>{
	
	@Query(value="select max(l.id_Lot) from Lot l where l.status = ?1",nativeQuery = true)
	Long findMaxIdByStatus(String status);

	@Query(value="select t.* from (select l.* from Lot l where l.status = ?1 order by l.date_Envoi desc ) t where rownum=1",nativeQuery = true)
	Lot findLastLotByStatus(String status);
}
