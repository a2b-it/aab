package ma.cam.service;

import java.util.LinkedHashMap;
import java.util.List;

import ma.cam.dto.UserDto;
import ma.cam.dto.UserResult;
import ma.cam.model.MessageOracle;
import ma.cam.model.Utilisateur;

public interface UserService {

	Utilisateur save(UserDto user);

	List<Utilisateur> findAll();

	void delete(int id);

	Utilisateur findOne(String username);

	Utilisateur findById(int id);

	List<Utilisateur> findAllUsersByQuery();

	UserResult getInfoUtilisateur(Long idUtilisateur);

	List<Utilisateur> myStoredProcedureComplexe(String paramIn,LinkedHashMap<String, Object> rs)  throws Exception;
	
	public MessageOracle majWithStoredProcedure(String user,Utilisateur utilisateur, String mode)  throws Exception;

}
