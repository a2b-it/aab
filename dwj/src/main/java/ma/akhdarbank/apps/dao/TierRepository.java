package ma.akhdarbank.apps.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import ma.akhdarbank.apps.model.LabFMatching;
import ma.akhdarbank.apps.model.Tiers;

@Repository
public interface TierRepository extends CrudRepository<Tiers, Long>, PagingAndSortingRepository<Tiers, Long> {
	
	
	@Query(
		  value = " select IDENTIFIANT,TYPEPERSONNE,NOM,PRENOM,RAISONSOCIALE,ETX_RIM from tiers ORDER BY IDENTIFIANT", 
		  countQuery = "SELECT count(*) FROM tiers", 
		  nativeQuery = true)
	Page<Tiers> findAllTiersWithPagination(Pageable pageable);
	
	
	
	@Query(value = "insert into LAB_F_MATCHING (ETX_RIM, NOM, PRENOM, ID_BATCH) values(:idclient, :nom, :prenom, :idbatch")
	void Insert(LabFMatching batchTiers);
	
}
