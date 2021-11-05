/**
 * 
 */
package ma.alakhdarbank.ccb.persistence;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ma.alakhdarbank.ccb.entity.Ctr;

/**
 * @author a.bouabidi
 *
 */
@Repository
public interface CtrRepository extends CrudRepository<Ctr, Integer>{
	
	
}
