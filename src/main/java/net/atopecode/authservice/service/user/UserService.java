package net.atopecode.authservice.service.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.atopecode.authservice.model.rel_user_role.IRelUserRoleRepository;
import net.atopecode.authservice.model.rel_user_role.RelUserRole;
import net.atopecode.authservice.model.role.Role;
import net.atopecode.authservice.model.role.dto.RoleDto;
import net.atopecode.authservice.model.user.IUserRepository;
import net.atopecode.authservice.model.user.User;
import net.atopecode.authservice.model.user.converter.UserDtoToUserConverter;
import net.atopecode.authservice.model.user.dto.UserDto;
import net.atopecode.authservice.service.role.IRoleService;
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
	private IRoleService roleService;
	private IRelUserRoleRepository relUserRoleRepository;
	
	@Autowired
	public UserService(IUserRepository userRepository,
			IUserQueryService userQueryService,
			UserValidatorComponent userValidation,
			UserDtoToUserConverter userDtoToUserConverter,
			IRoleService roleService,
			IRelUserRoleRepository relUserRoleRepository) {
		this.userRepository = userRepository;
		this.userQueryService = userQueryService;
		this.userValidator = userValidation;
		this.userDtoToUserConverter = userDtoToUserConverter;
		this.roleService = roleService;
		this.relUserRoleRepository = relUserRoleRepository;
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
		userValidator.validateInsertDto(userDto);
		
		User user = userDtoToUserConverter.convert(userDto);
		user.normalize();	
		user = userRepository.save(user);
		
		setRolesToUser(user.getId(), userDto.getRoles());
		
		return user;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public User update(UserDto userDto) throws ValidationException {
		User user = userValidator.validateUpdateDto(userDto);
		
		userDtoToUserConverter.map(userDto, user);
		user.normalize();
		user = userRepository.save(user);
		
		setRolesToUser(user.getId(), userDto.getRoles());
		
		return user;
	}

	@Override
    @Transactional(rollbackFor = Exception.class)
	public User setRolesToUser(Long idUser, Set<RoleDto> rolesDto) throws ValidationException {
		if(idUser == null) {
			throw new UserServiceRuntimeException("No se pueden asignar 'Roles' al 'User' porque no se ha recibido valor para el parámetro 'idUser'");
		}
			
		if(rolesDto == null) {
			throw new UserServiceRuntimeException("No se pueden asignar 'Roles' al 'User' porque no se ha recibido valor para el parámetro 'rolesDto'");
		}		
		
		User user = userValidator.checkExistsUser(idUser);
		List<Role> roles = userValidator.checkExistsRoles(rolesDto);
		List<Role> userRoles = roleService.findRolesByUser(user);
		List<Role> rolesToInsert = new ArrayList<>();
		List<Role> rolesToDelete = new ArrayList<>();
		
		for(Role role: roles) {
			if(!userRoles.contains(role)) {
				rolesToInsert.add(role);
			}
		}
		
		for(Role role: userRoles) {
			if(!roles.contains(role)) {
				rolesToDelete.add(role);
			}
		}
		
		rolesToInsert.forEach(role -> {
			RelUserRole relUserRole = new RelUserRole(user, role);
			relUserRoleRepository.save(relUserRole);
		});
		
		rolesToDelete.forEach(role -> {
			RelUserRole relUserRole = relUserRoleRepository.findByUserAndRole(user, role).orElse(null);
			if(relUserRole != null) {
				relUserRoleRepository.delete(relUserRole);
			}
		});
		
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
	
	@Override
	public Page<User> findAll(PageRequest pageRequest){
		return userQueryService.findAll(pageRequest);
	}

	
	//TODO...
	//-Añadir plugin eclipse para editar los archivos .properties y que escape los acentos automáticamente en los mensajes de locale.
	//-Añadir Swagger.
	//-Modificar empaquetado de las clases para cuando se configure por módulos. Todo lo del User dentro del package 'user' y así sucesivamente.
	//-Hacer tests utilizando otra B.D. de prueba.
	//-Usar Specifications para querys con filtro.
	//-JpaAuditing.
	//-Spring Security con User creado por defecto que sea Admin y al que posteriormente se le cambie el password.
	//-Añadir Módulos de Java 11.
	
	public static class UserServiceRuntimeException extends RuntimeException {
		private static final long serialVersionUID = -1509839006573338505L;

		public UserServiceRuntimeException(String message) {
			super(message);
		}
	}
}
