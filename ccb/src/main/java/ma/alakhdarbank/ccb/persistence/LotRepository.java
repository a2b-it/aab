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
	Long findMaxNLotByStatus(int status);

	@Query(value="select t.* from (select l.* from Lot l where l.status = ?1 or l.status = ?2 order by l.date_Envoi desc ) t where rownum=1",nativeQuery = true)
	Lot findLastLotByStatus(int env, int enc);
	
	@Query(value="select count(l.*) from Lot l where l.status = ?1 ",nativeQuery = true)
	int findCountByStatus(int status);
	
	@Query(value="select * from Lot l where l.id_lot = ?1 ",nativeQuery = true)
	Lot findByIdLot(Long id);
}
