package net.atopecode.authservice.user.service.validator;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.atopecode.authservice.localization.MessageLocalized;
import net.atopecode.authservice.role.dto.RoleDto;
import net.atopecode.authservice.role.model.Role;
import net.atopecode.authservice.role.service.query.IRoleQueryService;
import net.atopecode.authservice.user.dto.UserDto;
import net.atopecode.authservice.user.model.User;
import net.atopecode.authservice.user.model.UserFieldNames;
import net.atopecode.authservice.user.service.query.IUserQueryService;
import net.atopecode.authservice.validators.base.AbstractValidator;
import net.atopecode.authservice.validators.exception.ValidationException;

@Component
public class UserValidatorComponent extends AbstractValidator<User, UserDto> {
	
	//Codes of Locale '.properties' files:
	public static final String USER_VALIDATION_INSERT_NULL_OBJECT_VALUE = "user.validation.insert.null.object.value";
	public static final String USER_VALIDATION_INSERT_NOTNULL_ID = "user.validation.insert.notnull.id";
	public static final String USER_VALIDATION_INSERT_ID_ALREADY_EXISTS = "user.validation.insert.id.already.exists";
	public static final String USER_VALIDATION_INSERT_NAME_ALREADY_EXISTS = "user.validation.insert.name.already.exists";
	public static final String USER_VALIDATION_INSERT_EMAIL_ALREADY_EXISTS = "user.validation.insert.email.already.exists";
	public static final String USER_VALIDATION_UPDATE_NULL_OBJECT_VALUE = "user.validation.update.null.object.value";
	public static final String USER_VALIDATION_UPDATE_NULL_ID = "user.validation.update.null.id";
	public static final String USER_VALIDATION_UPDATE_ID_NOT_EXISTS = "user.validation.update.id.not.exists";
	public static final String USER_VALIDATION_UPDATE_NAME_ALREADY_EXISTS = "user.validation.update.name.already.exists";
	public static final String USER_VALIDATION_UPDATE_EMAIL_ALREADY_EXISTS = "user.validation.update.email.already.exists";
	public static final String USER_VALIDATION_NOT_EXISTS_ROLE = "user.validation.not.exists.role";
	public static final String USER_VALIDATION_ID_NOT_EXISTS = "user.validation.id.not.exists";
	
	//Regex Format rules:
	public static final String EMAIL_REGEX = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";

	private final IUserQueryService userQueryService;
	private final IRoleQueryService roleQueryService;
	
	@Autowired
	public UserValidatorComponent(IUserQueryService userQueryService,
			IRoleQueryService roleQueryService) {
		super(User.class);
		this.userQueryService = userQueryService;
		this.roleQueryService = roleQueryService;
	}
	
	/**
	 * Validaciones de campos y lógica de negocio a la hora de insertar un nuevo Usuario.
	 * @param user
	 * @throws ValidationException
	 */
	public void validateInsertDto(UserDto user) throws ValidationException {
		notNull(user, 
				new ValidationException("No se puede insertar el 'User' porque vale 'null'",
					new MessageLocalized(USER_VALIDATION_INSERT_NULL_OBJECT_VALUE)));
		
		mustToBeNull(user.getId(),
				new ValidationException("No se puede insertar el 'User' porque su 'id' no vale 'null'.",
					new MessageLocalized(USER_VALIDATION_INSERT_NOTNULL_ID)));
		
		validateFieldsDto(user);
		
		ifTrueThrows(() -> userQueryService.findById(user.getId()).isPresent(),
				new ValidationException("No se puede insertar al 'User' porque ya existe uno con el 'id': " + user.getId(),
						new MessageLocalized(USER_VALIDATION_INSERT_ID_ALREADY_EXISTS, user.getId())));
		
		ifTrueThrows(() -> userQueryService.findByName(user.getName()).isPresent(),
				new ValidationException("No se puede insertar al 'User' porque ya existe uno con el 'name': " + user.getName(),
						new MessageLocalized(USER_VALIDATION_INSERT_NAME_ALREADY_EXISTS, user.getName())));
		
		ifTrueThrows(() -> userQueryService.findByEmail(user.getEmail()).isPresent(),
				new ValidationException("No se puede insertar al 'User' porque ya existe uno con el 'email': " + user.getEmail(),
						new MessageLocalized(USER_VALIDATION_INSERT_EMAIL_ALREADY_EXISTS, user.getEmail())));		
	}
	
	public User validateUpdateDto(UserDto user) throws ValidationException {
		notNull(user,
				new ValidationException("No se puede modificar el 'User' porque vale 'null'",
					new MessageLocalized(USER_VALIDATION_UPDATE_NULL_OBJECT_VALUE)));

		notNull(user.getId(),
				new ValidationException("No se puede modificar el 'User' porque su 'id' vale 'null'.",
					new MessageLocalized(USER_VALIDATION_UPDATE_NULL_ID)));
		
		validateFieldsDto(user);
		
		final User userBd = userQueryService.findById(user.getId()).orElse(null);
		ifTrueThrows(() -> userBd == null,
				new ValidationException("No se puede modificar el 'User' porque no existe ninguno con id:" + user.getId() + " en la B.D.",
						new MessageLocalized(USER_VALIDATION_UPDATE_ID_NOT_EXISTS, user.getId())));
		
		if(!StringUtils.equals(user.getName(), userBd.getName())){
			ifTrueThrows(() -> userQueryService.findByName(user.getName()).isPresent(),
					new ValidationException("No se puede modificar el campo 'name' del 'User' con 'id': " + user.getId() + " porque ya existe en la .B.D. uno con el 'name': " + user.getName(),
							new MessageLocalized(USER_VALIDATION_UPDATE_NAME_ALREADY_EXISTS, user.getId(), user.getName())));
		}
		
		if(!StringUtils.equals(user.getEmail(), userBd.getEmail())){
			ifTrueThrows(() -> userQueryService.findByEmail(user.getEmail()).isPresent(),
					new ValidationException("No se puede modificar el campo 'email' del 'User' con 'id': " + user.getId() + " porque ya existe en la .B.D. uno con el 'email': " + user.getEmail(),
							new MessageLocalized(USER_VALIDATION_UPDATE_EMAIL_ALREADY_EXISTS, user.getId(), user.getEmail())));
		}
		
		return userBd;
	}
	
	public User checkExistsUser(Long idUser) throws ValidationException {
		if(idUser == null) {
			return null;
		}
		
		final User userBd = userQueryService.findById(idUser).orElse(null);
		ifTrueThrows(() -> userBd == null,
				new ValidationException("No existe el 'User' con id:" + idUser + " en la B.D.",
						new MessageLocalized(USER_VALIDATION_ID_NOT_EXISTS, idUser)));
		
		return userBd;
	}
	
	public List<Role> checkExistsRoles(Set<RoleDto> roles) throws ValidationException {
		List<Role> result = new ArrayList<>();
		if(roles == null) {
			return result;
		}
		
		for(RoleDto role: roles) {
			Role roleBd = roleQueryService.findById(role.getId()).orElse(null);
			ifTrueThrows(() -> roleBd == null, 
					new ValidationException("No se puede guardar el 'User' porque no existe el 'Role' con id: " + role.getId(),
							new MessageLocalized(USER_VALIDATION_NOT_EXISTS_ROLE, role.getId())));
			
			result.add(roleBd);
		}
		
		return result;
	}
	
	/**
	 * Validaciones de los campos del Usuario.
	 * @param user
	 * @throws ValidationException
	 */
	public void validateFieldsDto(UserDto user) throws ValidationException {		
		//Name:
		notEmpty(user.getName(), "No se puede guardar el 'User' porque el campo 'name' no tiene valor", UserFieldNames.NAME);
		
		maxLength(user.getName(), User.NAME_MAX_LENGHT,
				"No se puede guardar el 'User' porque el campo 'name' es muy largo", UserFieldNames.NAME);
		
		//Password:
		notEmpty(user.getPassword(), "No se puede guardar el 'User' porque el campo 'password' vale 'null'", UserFieldNames.PASSWORD);
		
		maxLength(user.getPassword(), User.PASSWORD_MAX_LENGHT, "No se puede guardar el 'User' porque el campo 'password' es muy largo", UserFieldNames.PASSWORD);

		//Email:
		notEmpty(user.getEmail(), "No se puede guardar el 'User' porque el campo 'email' vale 'null'", UserFieldNames.EMAIL);
		
		maxLength(user.getEmail(), User.EMAIL_MAX_LENGHT, "No se puede guardar el 'User' porque el campo 'email' es muy largo", UserFieldNames.EMAIL);
		
		hasFormat(user.getEmail(),EMAIL_REGEX, "No se puede guardar el 'User' porque el campo 'email' tiene un formato incorrecto", UserFieldNames.EMAIL);
		
		//RealName:
		maxLength(user.getRealName(), User.REAL_NAME_MAX_LENGHT, "No se puede guardar el 'User' porque el campo 'realName' es muy largo",  UserFieldNames.REAL_NAME);
		
		//Roles:
		notEmptyCollection(user.getRoles(), "No se puede guardar el 'User' porque el campo 'roles' es una lista vacía", UserFieldNames.Dto.ROLES);
	}
		
}
