package net.atopecode.authservice.service.user;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.atopecode.authservice.model.user.IUserRepository;
import net.atopecode.authservice.model.user.User;
import net.atopecode.authservice.model.user.converter.UserDtoToUserConverter;
import net.atopecode.authservice.model.user.dto.UserDto;
import net.atopecode.authservice.service.user.query.IUserQueryService;
import net.atopecode.authservice.service.user.validator.UserValidatorComponent;
import net.atopecode.authservice.validators.exception.ValidationException;

@Service
public class UserService implements IUserService {
	public static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

	
	private IUserRepository userRepository;
	private IUserQueryService userQueryService;
	private UserValidatorComponent userValidator;
	private UserDtoToUserConverter userDtoToUserConverter;
	
	@Autowired
	public UserService(IUserRepository userRepository,
			IUserQueryService userQueryService,
			UserValidatorComponent userValidation,
			UserDtoToUserConverter userDtoToUserConverter) {
		this.userRepository = userRepository;
		this.userQueryService = userQueryService;
		this.userValidator = userValidation;
		this.userDtoToUserConverter = userDtoToUserConverter;
	}


	@Override
	@Transactional(rollbackFor = Exception.class)
	public User save(UserDto userDto) throws ValidationException {
		User result = null;
		if(userDto == null) {
			return result;
		}
		
		if(userDto.getId() == null) {
			result = insert(userDto);
		}
		else {
			result = update(userDto);
		}
		
		return result;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public User insert(UserDto userDto) throws ValidationException {
		userValidator.validateInsert(userDto);
		
		User user = userDtoToUserConverter.convert(userDto);
		user = userRepository.save(user);
		
		return user;
	}


	@Override
	@Transactional(rollbackFor = Exception.class)
	public User update(UserDto userDto) throws ValidationException {
		User user = userValidator.validateUpdate(userDto);
		
		userDtoToUserConverter.map(userDto, user);
		user = userRepository.save(user);
		
		return user;
	}


	@Override
	public void delete(Long idUser) {
		if(idUser == null) {
			return;
		}
		
		User userToDelete = findById(idUser).orElse(null);
		if(userToDelete != null) {
			userRepository.delete(userToDelete);
		}
		
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
	//Manejador de Exceptiones no controladas en los Controllers. Y probar con peticiones Http el Controller a mano.
	//Hacer tests para esta clase utilizando otra B.D. de prueba.
	//Usar Specifications para querys con filtro.
	//JpaAuditing.
	//Spring Security con User creado por defecto que sea Admin y al que posteriormente se le cambie el password.
	//Añadir Módulos de Java 11.
}
