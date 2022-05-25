package net.atopecode.authservice.user.service.validator;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.atopecode.authservice.localization.messagelocalized.MessageLocalized;
import net.atopecode.authservice.role.dto.RoleDto;
import net.atopecode.authservice.role.model.Role;
import net.atopecode.authservice.role.service.query.IRoleQueryService;
import net.atopecode.authservice.user.dto.UserDto;
import net.atopecode.authservice.user.model.User;
import net.atopecode.authservice.user.model.UserFieldNames;
import net.atopecode.authservice.user.service.query.IUserQueryService;
import net.atopecode.authservice.validation.ValidationMessageLocalized;
import net.atopecode.authservice.validation.Validator;
import net.atopecode.authservice.validation.exceptions.ValidationException;


@Component
public class UserValidatorComponent extends Validator {
	
	//Códigos de archivos locale 'messages.properties' para traducción de los mensajes de error en las validaciones del 'Usuario'.
	public static final String USER_VALIDATION_INSERT_ID_ALREADY_EXISTS = "user.validation.insert.id.already.exists";
	public static final String USER_VALIDATION_INSERT_NAME_ALREADY_EXISTS = "user.validation.insert.name.already.exists";
	public static final String USER_VALIDATION_INSERT_EMAIL_ALREADY_EXISTS = "user.validation.insert.email.already.exists";
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
		super();
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
				new UserValidationException("No se puede insertar el 'User' porque vale 'null'",
						ValidationMessageLocalized.forNotNull(UserFieldNames.ENTITY)));
		
		mustToBeNull(user.getId(),
				new UserValidationException("No se puede insertar el 'User' porque su 'id' no vale 'null'.",
						ValidationMessageLocalized.forMustToBeNull(UserFieldNames.ID)));
		
		validateFieldsDto(user);
		
		ifTrueThrows(() -> userQueryService.findById(user.getId()).isPresent(),
				new UserValidationException("No se puede insertar al 'User' porque ya existe uno con el 'id': " + user.getId(),
						new MessageLocalized(USER_VALIDATION_INSERT_ID_ALREADY_EXISTS, user.getId())));
		
		ifTrueThrows(() -> userQueryService.findByName(user.getName()).isPresent(),
				new UserValidationException("No se puede insertar al 'User' porque ya existe uno con el 'name': " + user.getName(),
						new MessageLocalized(USER_VALIDATION_INSERT_NAME_ALREADY_EXISTS, user.getName())));
		
		ifTrueThrows(() -> userQueryService.findByEmail(user.getEmail()).isPresent(),
				new UserValidationException("No se puede insertar al 'User' porque ya existe uno con el 'email': " + user.getEmail(),
						new MessageLocalized(USER_VALIDATION_INSERT_EMAIL_ALREADY_EXISTS, user.getEmail())));		
	}
	
	public User validateUpdateDto(UserDto user) throws ValidationException {
		notNull(user,
				new UserValidationException("No se puede modificar el 'User' porque vale 'null'",
						ValidationMessageLocalized.forNotNullValue(UserFieldNames.ENTITY)));

		notNull(user.getId(),
				new UserValidationException("No se puede modificar el 'User' porque su 'id' vale 'null'.",
						ValidationMessageLocalized.forNotNull(UserFieldNames.ID)));
		
		validateFieldsDto(user);
		
		final User userBd = userQueryService.findById(user.getId()).orElse(null);
		ifTrueThrows(() -> userBd == null,
				new UserValidationException("No se puede modificar el 'User' porque no existe ninguno con id:" + user.getId() + " en la B.D.",
						new MessageLocalized(USER_VALIDATION_UPDATE_ID_NOT_EXISTS, user.getId())));
		
		if(!StringUtils.equals(user.getName(), userBd.getName())){
			ifTrueThrows(() -> userQueryService.findByName(user.getName()).isPresent(),
					new UserValidationException("No se puede modificar el campo 'name' del 'User' con 'id': " + user.getId() + " porque ya existe en la .B.D. uno con el 'name': " + user.getName(),
							new MessageLocalized(USER_VALIDATION_UPDATE_NAME_ALREADY_EXISTS, user.getId(), user.getName())));
		}
		
		if(!StringUtils.equals(user.getEmail(), userBd.getEmail())){
			ifTrueThrows(() -> userQueryService.findByEmail(user.getEmail()).isPresent(),
					new UserValidationException("No se puede modificar el campo 'email' del 'User' con 'id': " + user.getId() + " porque ya existe en la .B.D. uno con el 'email': " + user.getEmail(),
							new MessageLocalized(USER_VALIDATION_UPDATE_EMAIL_ALREADY_EXISTS, user.getId(), user.getEmail())));
		}
		
		return userBd;
	}
	
	public User checkExistsUser(Long idUser) throws ValidationException {
		if(idUser == null) {
			return null;
		}
		
		final User userBd = userQueryService.findById(idUser).orElse(null);
		ifTrueThrows(() -> (userBd == null),
				new UserValidationException("No existe el 'User' con id:" + idUser + " en la B.D.",
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
			ifTrueThrows(() -> (roleBd == null), 
					new UserValidationException("No existe el 'Role' con id: " + role.getId(),
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
		String userName = user.getName();
		notEmpty(userName,
				new UserValidationException("No se puede guardar el 'User' porque el campo 'name' no tiene valor",
						ValidationMessageLocalized.forNotEmpty(UserFieldNames.NAME)));
		
		maxLength(userName, User.NAME_MAX_LENGHT,
				new UserValidationException("No se puede guardar el 'User' porque el campo 'name' es muy largo",
						ValidationMessageLocalized.forMaxLength(UserFieldNames.NAME, userName, User.NAME_MAX_LENGHT)));
		
		//Password:
		String userPassword = user.getPassword();
		notEmpty(userPassword,
				new UserValidationException("No se puede guardar el 'User' porque el campo 'password' vale 'null'",
						ValidationMessageLocalized.forNotEmpty(UserFieldNames.PASSWORD)));
		
		maxLength(userPassword, User.PASSWORD_MAX_LENGHT,
				new UserValidationException("No se puede guardar el 'User' porque el campo 'password' es muy largo",
						ValidationMessageLocalized.forMaxLength(UserFieldNames.PASSWORD, userPassword, User.PASSWORD_MAX_LENGHT)));

		//Email:
		String userEmail = user.getEmail();
		notEmpty(userEmail,
				new UserValidationException("No se puede guardar el 'User' porque el campo 'email' vale 'null'",
						ValidationMessageLocalized.forNotEmpty(UserFieldNames.EMAIL)));
		
		maxLength(userEmail, User.EMAIL_MAX_LENGHT,
				new UserValidationException("No se puede guardar el 'User' porque el campo 'email' es muy largo",
						ValidationMessageLocalized.forMaxLength(UserFieldNames.EMAIL, userEmail, User.EMAIL_MAX_LENGHT)));
		
		hasFormat(userEmail, EMAIL_REGEX,
				new UserValidationException("No se puede guardar el 'User' porque el campo 'email' tiene un formato incorrecto",
						ValidationMessageLocalized.forHasFormat(UserFieldNames.EMAIL, userEmail)));
		
		//RealName:
		String userRealName = user.getRealName();
		maxLength(userRealName, User.REAL_NAME_MAX_LENGHT,
				new UserValidationException("No se puede guardar el 'User' porque el campo 'realName' es muy largo",
						ValidationMessageLocalized.forMaxLength(UserFieldNames.REAL_NAME, userRealName, User.REAL_NAME_MAX_LENGHT)));
		
		//Roles:
		notEmptyCollection(user.getRoles(), 
				new UserValidationException("No se puede guardar el 'User' porque el campo 'roles' es una lista vacía",
						ValidationMessageLocalized.forNotEmptyCollection(UserFieldNames.Dto.ROLES)));
	}
	
	
	public static class UserValidationException extends ValidationException {

		
		public UserValidationException(String logMessage, MessageLocalized errorMessage) {
			super(logMessage, errorMessage);
		}
	}
		
}
