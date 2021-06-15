package net.atopecode.authservice.service.user;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.validation.Validator;

import net.atopecode.authservice.model.user.IUserRepository;
import net.atopecode.authservice.model.user.User;
import net.atopecode.authservice.model.user.UserDto;
import net.atopecode.authservice.util.NormalizeString;

@Service
public class UserService implements IUserService {
	public static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

	
	private IUserRepository userRepository;
	
	@Autowired
	public UserService(IUserRepository userRepository) {
		this.userRepository = userRepository;
	}


	@Override
	@Transactional(rollbackFor = Exception.class)
	public User save(UserDto user) {
		User result = null;
		if(user == null) {
			return result;
		}
		
		if(user.getId() == null) {
			result = insert(user);
		}
		else {
			result = update(user);
		}
		
		return result;
	}

	private User insert(UserDto user) {
		
		
		
		return null;
	}

	private User update(UserDto user) {
		// TODO Auto-generated method stub
		return null;
	} 
	
	@Override
	public Optional<User> findById(Long id) {
		Optional<User> user = Optional.empty();
		if(id != null) {
			user = userRepository.findById(id);
		}
		
		return user;
	}

	@Override
	public Optional<User> findByIdWithRoles(Long id) {
		Optional<User> user = Optional.empty();
		if(id != null) {
			user = userRepository.findByIdWithRoles(id);
		}
		
		return user;
	}

	@Override
	public Optional<User> findByName(String name) {
		Optional<User> user = Optional.empty();
		if(StringUtils.hasText(name)) {
			name = NormalizeString.normalize(name);
			user = userRepository.findByNormalizedName(name);
		}
		
		return user;
	}

	@Override
	public Optional<User> findByNameWithRoles(String name) {
		Optional<User> user = Optional.empty();
		if(StringUtils.hasText(name)) {
			name = NormalizeString.normalize(name);
			user = userRepository.findByNormalizedNameWithRoles(name);
		}
		
		return user;
	}

	@Override
	public Optional<User> findByEmail(String email) {
		Optional<User> user = Optional.empty();
		if(StringUtils.hasText(email)) {
			email = NormalizeString.normalize(email);
			user = userRepository.findByNormalizedEmail(email);
		}
		
		return user;
	}

	@Override
	public Optional<User> findByEmailWithRoles(String email) {
		Optional<User> user = Optional.empty();
		if(StringUtils.hasText(email)) {
			email = NormalizeString.normalize(email);
			user = userRepository.findByNormalizedEmailWithRoles(email);
		}
		
		return user;
	}

	
	//TODO...
	//Hacer tests para esta clase utilizando otra B.D. de prueba.
	//Usar Specifications para querys con filtro.
	//Usar Validaciones para guardar registros en la B.D. en la capa de Servicio solo.
	//Manejador de Exceptiones no controladas en los Controllers.
	//JpaAuditing.
	//Spring Security con User creado por defecto que sea Admin y al que posteriormente se le cambie el password.
	
}
