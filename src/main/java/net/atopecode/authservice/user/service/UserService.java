package net.atopecode.authservice.user.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.Persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.atopecode.authservice.rel_user_role.model.RelUserRole;
import net.atopecode.authservice.rel_user_role.repository.IRelUserRoleRepository;
import net.atopecode.authservice.role.dto.RoleDto;
import net.atopecode.authservice.role.model.Role;
import net.atopecode.authservice.role.service.IRoleService;
import net.atopecode.authservice.user.converter.UserDtoToUserConverter;
import net.atopecode.authservice.user.dto.UserDto;
import net.atopecode.authservice.user.dto.filter.UserFilter;
import net.atopecode.authservice.user.model.User;
import net.atopecode.authservice.user.repository.IUserRepository;
import net.atopecode.authservice.user.service.exceptions.UserNotFoundException;
import net.atopecode.authservice.user.service.query.IUserQueryService;
import net.atopecode.authservice.user.service.validator.UserValidationException;
import net.atopecode.authservice.user.service.validator.UserValidatorComponent;
import net.atopecode.authservice.validation.exceptions.ValidationException;


@Service
public class UserService implements IUserService {
	public static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

	
	private IUserRepository userRepository;
	private IUserQueryService userQueryService;
	private UserValidatorComponent userValidator;
	private UserDtoToUserConverter userDtoToUserConverter;
	private IRoleService roleService;
	private IRelUserRoleRepository relUserRoleRepository;
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	public UserService(IUserRepository userRepository,
			IUserQueryService userQueryService,
			UserValidatorComponent userValidation,
			UserDtoToUserConverter userDtoToUserConverter,
			IRoleService roleService,
			IRelUserRoleRepository relUserRoleRepository,
			BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.userRepository = userRepository;
		this.userQueryService = userQueryService;
		this.userValidator = userValidation;
		this.userDtoToUserConverter = userDtoToUserConverter;
		this.roleService = roleService;
		this.relUserRoleRepository = relUserRoleRepository;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder; 
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
	public User insert(UserDto userDto) throws UserValidationException {
		userValidator.validateInsertDto(userDto);
		
		User user = userDtoToUserConverter.convert(userDto);
		user.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
		user.normalize();	
		user = userRepository.save(user);
		
		setRolesToUser(user.getId(), userDto.getRoles());
		
		return user;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public User update(UserDto userDto) throws UserValidationException {
		User user = userValidator.validateUpdateDto(userDto);
		
		userDtoToUserConverter.map(userDto, user);
		user.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
		user.normalize();
		user = userRepository.save(user);
		
		setRolesToUser(user.getId(), userDto.getRoles());
		
		return user;
	}

	@Override
    @Transactional(rollbackFor = Exception.class)
	public User setRolesToUser(Long idUser, Set<RoleDto> rolesDto) throws UserValidationException {
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
	public List<Role> getRoles(User user){
		final List<Role> result = new ArrayList<>();
		if(user == null) {
			return result;
		}
		
		if(!hasRolesLoaded(user)) {
			result.addAll(roleService.findRolesByUser(user));
		}
		else {
			user.getRelUserRole()
				.forEach(relUserRole -> result.add(relUserRole.getRole()));
		}
		
		return result;
	}
	
	private boolean hasRolesLoaded(User user) {
		return Persistence.getPersistenceUtil().isLoaded(user.getRelUserRole());
	}
	
	@Override
	public void delete(Long idUser) {
		if(idUser == null) {
			return;
		}
		
		User userToDelete = userQueryService.findById(idUser).orElse(null);
		if(userToDelete != null) {
			userRepository.delete(userToDelete);
		}		
	}


	@Override
	public User findById(Long id) throws UserNotFoundException {
		return userQueryService.findById(id).orElseThrow(() -> new UserNotFoundException(id.toString()));
	}


	@Override
	public User findByIdWithRoles(Long id) throws UserNotFoundException {
		return userQueryService.findByIdWithRoles(id).orElseThrow(() -> new UserNotFoundException(id.toString()));
	}


	@Override
	public User findByName(String name) throws UserNotFoundException {
		return userQueryService.findByName(name).orElseThrow(() -> new UserNotFoundException(name));
	}


	@Override
	public User findByNameWithRoles(String name) throws UserNotFoundException {
		return userQueryService.findByNameWithRoles(name).orElseThrow(() -> new UserNotFoundException(name));
	}


	@Override
	public User findByEmail(String email) throws UserNotFoundException {
		return userQueryService.findByEmail(email).orElseThrow(() -> new UserNotFoundException(email)
				);
	}


	@Override
	public User findByEmailWithRoles(String email) throws UserNotFoundException {
		return userQueryService.findByEmailWithRoles(email).orElseThrow(() -> new UserNotFoundException(email));
	}
	
	@Override
	public Page<User> findAll(PageRequest pageRequest) {
		return userQueryService.findAll(pageRequest);
	}

	@Override
	public Page<User> findAllWithRoles(PageRequest pageRequest) {
		return userQueryService.findAllWithRoles(pageRequest);
	}
	
	@Override
	public Page<User> query(UserFilter filter) throws ValidationException {
		return userQueryService.query(filter);
	}
		
	public static class UserServiceRuntimeException extends RuntimeException {
		private static final long serialVersionUID = -1509839006573338505L;

		public UserServiceRuntimeException(String message) {
			super(message);
		}
	}
}
