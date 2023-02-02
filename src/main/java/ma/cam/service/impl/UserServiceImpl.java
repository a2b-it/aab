package ma.cam.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import ma.cam.commun.Constants;
import ma.cam.commun.Util;
import ma.cam.dao.AbstractDAO;
import ma.cam.dao.UserDao;
import ma.cam.dto.UserDto;
import ma.cam.dto.UserResult;
import ma.cam.model.MessageOracle;
import ma.cam.model.ParamPs;
import ma.cam.model.Utilisateur;
import ma.cam.service.UserService;

@Service(value = "userService")
public class UserServiceImpl extends AbstractDAO implements UserDetailsService, UserService {

	@Autowired
	private UserDao userDao;

	@Autowired
	private BCryptPasswordEncoder bcryptEncoder;

	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Utilisateur user = userDao.findByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException("Invalid username or password.");
		}
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getMotPasse(),
				getAuthority(user));
	}

	private Set<SimpleGrantedAuthority> getAuthority(Utilisateur user) {
		Set<SimpleGrantedAuthority> authorities = new HashSet<>();
		user.getRoles().forEach(role -> {
			authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
		});
		return authorities;
	}

	public List<Utilisateur> findAll() {
		List<Utilisateur> list = new ArrayList<>();
		userDao.findAll().iterator().forEachRemaining(list::add);
		return list;
	}

	@Override
	public void delete(int id) {
		userDao.deleteById(id);
	}

	@Override
	public Utilisateur findOne(String username) {
		return userDao.findByUsername(username);
	}

	@Override
	public Utilisateur findById(int id) {
		return userDao.findById(id).get();
	}

	@Override
	public Utilisateur save(UserDto user) {
		Utilisateur newUser = new Utilisateur();
		newUser.setUsername(user.getUsername());
		newUser.setMotPasse(bcryptEncoder.encode(user.getPassword()));
		return userDao.save(newUser);
	}

	@Override
	public List<Utilisateur> findAllUsersByQuery() {
//		return userDao.getUsersXML();
		return null;
	}

	@Override
	public UserResult getInfoUtilisateur(Long idUtilisateur) {
		return userDao.getInfoUtilisateur(idUtilisateur);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Utilisateur> myStoredProcedureComplexe(String paramIn, LinkedHashMap<String, Object> rs)
			throws Exception {
		final List<ParamPs> params = new ArrayList<ParamPs>();
		params.add(new ParamPs("V_PARAMETERS", Constants.IN, 1, Constants.T_ELEMENT, rs));
		params.add(new ParamPs(Constants.CURSOR_NAME, Constants.OUT, 2, Constants.CURSOR, null));
		params.add(new ParamPs("V_TYPEMESSAGE", Constants.OUT, 3, Constants.STRING, null));
		params.add(new ParamPs("V_MESSAGE", Constants.OUT, 4, Constants.STRING, null));
		return (List<Utilisateur>) executeQueryProcedure("PK_TEST_WS", "QR_GET_TIERS_LIST", Utilisateur.class, params)
				.getList();
	}

	public MessageOracle majWithStoredProcedure(String user, Utilisateur utilisateur, String mode) throws Exception {

		Map<String, Object> map = new HashMap<String, Object>();

		if (!Util.isNull(utilisateur)) {
			map = convertToMapWidthNull(utilisateur);
		}
		final List<ParamPs> params = new ArrayList<ParamPs>();
		params.add(new ParamPs("V_USER", Constants.IN, 1, Constants.STRING, user));
		params.add(new ParamPs("V_TYPEMAJ", Constants.IN, 2, Constants.STRING, mode));
		params.add(new ParamPs("V_FLAG_COMMIT", Constants.IN, 3, Constants.STRING, Constants.FLAG_OUI));
		params.add(new ParamPs("V_UTILISATEUR", Constants.IN, 4, Constants.T_ELEMENT, map));
		params.add(new ParamPs("V_TYPEMESSAGE", Constants.OUT, 5, Constants.STRING, Constants.EMPTY_STRING));
		params.add(new ParamPs("V_MESSAGE", Constants.OUT, 6, Constants.STRING, Constants.EMPTY_STRING));

		return executeProcedure("PK_TEST_WS", "PR_user", params);
	}

}
