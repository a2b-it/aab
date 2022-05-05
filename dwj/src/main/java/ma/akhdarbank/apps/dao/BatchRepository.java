/**
 * 
 */
package ma.akhdarbank.apps.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.akhdarbank.apps.model.LabFMatchingBatch;

/**
 * @author a.bouabidi
 *
 */
@Repository
public interface BatchRepository extends JpaRepository<LabFMatchingBatch, Long>{

	@Query (value="select * from LAB_F_MATCHING_BATCH L1 where statut = 'AT' and not exists (select * from LAB_F_MATCHING_BATCH L2 where l1.NUMTICKET=l2.NUMTICKET and l2.identifiant<>l1.identifiant and l1.statut<>'VA')", nativeQuery = true)
	List<LabFMatchingBatch> findAllActiveBatch();
}
