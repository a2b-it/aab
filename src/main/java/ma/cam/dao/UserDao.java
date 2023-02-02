package ma.cam.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ma.cam.dto.UserResult;
import ma.cam.model.Utilisateur;

@Repository
public interface UserDao extends CrudRepository<Utilisateur, Integer> {

	Utilisateur findByUsername(String username);

	@Query(value = "select * from utilisateur", nativeQuery = true)
	List<Utilisateur> findAllUsersByQuery();

	@Query(value = "select * from ws_utilisateur ut where ut.id = :idUtilisateur", nativeQuery = true)
	UserResult getInfoUtilisateur(@Param("idUtilisateur") Long idUtilisateur);
	

}
