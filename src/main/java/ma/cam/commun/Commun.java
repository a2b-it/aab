package ma.cam.commun;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import ma.cam.model.Utilisateur;
import ma.cam.service.UserService;

public class Commun {
	
	@Autowired
    private UserService userService;

	public Utilisateur getCurrentUtilisateur() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	String username="";
    	if (principal instanceof UserDetails) {
    	   username = ((UserDetails)principal).getUsername();
    	} else {
    	   username = principal.toString();
    	}
    	return userService.findOne(username);
	}
	
	public Long getIdCurrentUtilisateur() {
		Utilisateur user = getCurrentUtilisateur();
    	return user.getId();
	}
	
	
}
