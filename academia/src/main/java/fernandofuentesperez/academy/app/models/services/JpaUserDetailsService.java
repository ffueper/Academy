package fernandofuentesperez.academy.app.models.services;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fernandofuentesperez.academy.app.models.entities.Role;
import fernandofuentesperez.academy.app.models.entities.User;
import fernandofuentesperez.academy.app.models.repositories.UserDAO;

@Service("jpaUserDetailsService")
public class JpaUserDetailsService implements UserDetailsService {

	@Autowired
	private UserDAO userDAO;
	
	private Logger logger = LoggerFactory.getLogger(JpaUserDetailsService.class);
	
	@Override
	@Transactional(readOnly=true)
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		
		User user = userDAO.findByUserName(userName);
		
		if(user==null) {
			logger.error("Error login: no existe el usuario '"+ userName + "'");
			throw new UsernameNotFoundException("UserName '"+userName+"' no existe en e sistema.");
		}
		
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		
		for(Role role : user.getRoles()) {
			logger.info("Role: ".concat(role.getAuthority()));
			authorities.add(new SimpleGrantedAuthority(role.getAuthority()));
		}
		
		if(authorities.isEmpty()) {
			logger.error("Error login: usuario '"+ userName + "' no tiene roles asignados.");
			throw new UsernameNotFoundException("UserName '"+userName+"' no tiene roles asignados.");
		}
		
		return new org.springframework.security.core.userdetails
				.User(user.getUserName(), user.getPassword(), user.getEnabled(), 
						true, true, true, authorities);
	}

}
