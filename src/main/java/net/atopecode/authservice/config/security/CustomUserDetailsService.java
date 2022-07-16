package net.atopecode.authservice.config.security;


import java.util.List;
import java.util.stream.Collectors;

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


    private IUserService userService;

    @Autowired
    public CustomUserDetailsService(IUserService userService){
        this.userService = userService;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String usernameOrEmail)
            throws UsernameNotFoundException {
        // Let people login with either username or email
    	User user = null;
    	try {
    		user = userService.findByNameWithRoles(usernameOrEmail); //TODO... Hacer métodoq que busque al user por 'name' o 'mail'.
    	}
    	catch(UserNotFoundException ex) {
    		throw new UsernameNotFoundException("No existe ningún 'Usuario' con 'name' o 'email': " + usernameOrEmail);
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
