package net.atopecode.authservice.config.security;


import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.atopecode.authservice.rel_user_role.model.RelUserRole;
import net.atopecode.authservice.role.model.Role;
import net.atopecode.authservice.user.model.User;
import net.atopecode.authservice.user.service.IUserService;
import net.atopecode.authservice.user.service.exceptions.UserNotFoundException;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    public static final Logger LOGGER = LoggerFactory.getLogger(CustomUserDetailsService.class);

    private IUserService userService;

    @Autowired
    public CustomUserDetailsService(IUserService userService){
        this.userService = userService;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        // Let people login with either username or email
    	User user = null;
    	try {
    		user = userService.findByNameOrEmailWithRoles(usernameOrEmail);
    	}
    	catch(UserNotFoundException ex) {
            String message = MessageFormat.format("Se ha intentado hacer Logging (obtener token JWT) con un usuario o email que no existe: {0}", usernameOrEmail);
            LOGGER.warn(message);
    		throw new UsernameNotFoundException(message); //Cuando se lanza esta Exception, Spring Security acaba lanzando una 'BadCredentialsException'.
    	}

        List<Role> roles = getUserRoles(user);
        UserPrincipal userPrincipal = UserPrincipal.create(user, roles);
        return userPrincipal;
    }

    // This method is used by JWTAuthenticationFilter
    @Transactional
    public UserDetails loadUserById(Long id) throws UserNotFoundException {
        User user = userService.findByIdWithRoles(id);
        List<Role> roles = getUserRoles(user);
        
        return UserPrincipal.create(user, roles);
    }
    
	private List<Role> getUserRoles(User user) {
		return user.getRelUserRole().stream()
				.map(RelUserRole::getRole)
				.collect(Collectors.toList());
	}
}
