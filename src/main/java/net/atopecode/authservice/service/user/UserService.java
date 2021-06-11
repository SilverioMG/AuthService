package net.atopecode.authservice.service.user;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import net.atopecode.authservice.model.user.IUserRepository;
import net.atopecode.authservice.model.user.User;
import net.atopecode.authservice.model.user.UserDto;
import net.atopecode.authservice.util.NormalizeString;

@Service
public class UserService implements IUserService {
	public static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
	
	private IUserRepository userRepository;

	@Override
	public Optional<User> findById(Long id) {
		Optional<User> result = Optional.empty();
		if(id != null) {
			result = userRepository.findById(id);
		}
		
		return result;
	}

	@Override
	public Optional<User> findByIdWithRoles(Long id) {
		Optional<User> result = Optional.empty();
		if(id != null) {
			result = userRepository.findByIdWithRoles(id);
		}
		
		return result;
	}

	@Override
	public Optional<User> findByName(String name) {
		Optional<User> result = Optional.empty();
		if(!StringUtils.hasLength(name)) {
			name = NormalizeString.normalize(name);
			result = userRepository.findByNormalizeName(name);
		}
		
		return result;
	}

	@Override
	public Optional<User> findByNameWithRoles(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<User> findByEmail(String email) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<User> findByEmailWithRoles(String email) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User save(UserDto user) {
		// TODO Auto-generated method stub
		return null;
	}

	private User insert(UserDto user) {
		// TODO Auto-generated method stub
		return null;
	}

	private User update(UserDto user) {
		// TODO Auto-generated method stub
		return null;
	} 
	
	//TODO...
	//Hacer tests para esta clase utilizando otra B.D. de prueba.
	//Usar Specifications para querys con filtro.
	//Usar Validaciones para guardar registros en la B.D. en la capa de Servicio solo.
	
}
