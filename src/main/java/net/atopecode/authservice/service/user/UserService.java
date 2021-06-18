package net.atopecode.authservice.service.user;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.atopecode.authservice.model.user.IUserRepository;
import net.atopecode.authservice.model.user.User;
import net.atopecode.authservice.model.user.dto.UserDto;
import net.atopecode.authservice.service.user.query.IUserQueryService;
import net.atopecode.authservice.service.user.validator.UserValidatorComponent;

@Service
public class UserService implements IUserService {
	public static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

	
	private IUserRepository userRepository;
	private IUserQueryService userQueryService;
	private UserValidatorComponent userValidator;
	
	@Autowired
	public UserService(IUserRepository userRepository,
			IUserQueryService userQueryService,
			UserValidatorComponent userValidation) {
		this.userRepository = userRepository;
		this.userQueryService = userQueryService;
		this.userValidator = userValidation;
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

	@Override
	public User insert(UserDto user) {
		// TODO Converter de UserDTO a User, validaciones y guardar en B.D.
		return null;
	}


	@Override
	public User update(UserDto user) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void delete(Long idUser) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public Optional<User> findById(Long id) {
		return userQueryService.findById(id);
	}


	@Override
	public Optional<User> findByIdWithRoles(Long id) {
		return userQueryService.findByIdWithRoles(id);
	}


	@Override
	public Optional<User> findByName(String name) {
		return userQueryService.findByName(name);
	}


	@Override
	public Optional<User> findByNameWithRoles(String name) {
		return userQueryService.findByNameWithRoles(name);
	}


	@Override
	public Optional<User> findByEmail(String email) {
		return userQueryService.findByEmail(email);
	}


	@Override
	public Optional<User> findByEmailWithRoles(String email) {
		return userQueryService.findByEmailWithRoles(email);
	}

	
	//TODO...
	//Usar Validaciones para guardar registros en la B.D. en la capa de Servicio solo.
	//Manejador de Exceptiones no controladas en los Controllers.
	//Hacer tests para esta clase utilizando otra B.D. de prueba.
	//Usar Specifications para querys con filtro.
	//JpaAuditing.
	//Spring Security con User creado por defecto que sea Admin y al que posteriormente se le cambie el password.
	//Añadir Módulos de Java 11.
}
