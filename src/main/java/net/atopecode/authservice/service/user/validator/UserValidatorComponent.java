package net.atopecode.authservice.service.user.validator;


import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.atopecode.authservice.model.user.User;
import net.atopecode.authservice.model.user.dto.UserDto;
import net.atopecode.authservice.service.user.query.IUserQueryService;
import net.atopecode.authservice.validators.base.AbstractValidator;
import net.atopecode.authservice.validators.dto.ValidationError;
import net.atopecode.authservice.validators.exception.ValidationException;

@Component
public class UserValidatorComponent extends AbstractValidator {

	public static final String USER_VALIDATION_NOTNULL_FIELD = "user.validation.insert.notnull.field";
	public static final String USER_VALIDATION_NULL_FIELD = "user.validation.insert.null.field";
	public static final String USER_VALIDATION_MAXLENGTH_FIELD = "user.validation.insert.maxlength.field";
	
	public static final String USER_VALIDATION_INSERT_NULL_OBJECT_VALUE = "user.validation.insert.null.object.value";
	public static final String USER_VALIDATION_INSERT_ID_ALREADY_EXISTS = "user.validation.insert.id.already.exists";
	public static final String USER_VALIDATION_INSERT_NAME_ALREADY_EXISTS = "user.validation.insert.name.already.exists";
	public static final String USER_VALIDATION_INSERT_EMAIL_ALREADY_EXISTS = "user.validation.insert.email.already.exists";
	
	public static final String USER_VALIDATION_UPDATE_NULL_OBJECT_VALUE = "user.validation.update.null.object.value";
	public static final String USER_VALIDATION_UPDATE_ID_NOT_EXISTS = "user.validation.update.id.not.exists";
	public static final String USER_VALIDATION_UPDATE_NAME_ALREADY_EXISTS = "user.validation.update.name.already.exists";
	public static final String USER_VALIDATION_UPDATE_EMAIL_ALREADY_EXISTS = "user.validation.update.email.already.exists";

	private IUserQueryService userQueryService;
	
	@Autowired
	public UserValidatorComponent(IUserQueryService userQueryService) {
		this.userQueryService = userQueryService;
	}
	
	/**
	 * Validaciones de campos y lógica de negocio a la hora de insertar un nuevo Usuario.
	 * @param user
	 * @throws ValidationException
	 */
	public void validateInsert(UserDto user) throws ValidationException {
		notNull(user, 
				new ValidationException("No se puede insertar el 'User' porque vale 'null'",
					new ValidationError(USER_VALIDATION_INSERT_NULL_OBJECT_VALUE)));
		
		validateFields(user);
		
		ifTrueThrows(() -> userQueryService.findById(user.getId()).isPresent(),
				new ValidationException("No se puede insertar al 'User' porque ya existe uno con el 'id': " + user.getId(),
						new ValidationError(USER_VALIDATION_INSERT_ID_ALREADY_EXISTS, user.getId())));
		
		ifTrueThrows(() -> userQueryService.findByName(user.getName()).isPresent(),
				new ValidationException("No se puede insertar al 'User' porque ya existe uno con el 'name': " + user.getName(),
						new ValidationError(USER_VALIDATION_INSERT_NAME_ALREADY_EXISTS, user.getName())));
		
		ifTrueThrows(() -> userQueryService.findByEmail(user.getEmail()).isPresent(),
				new ValidationException("No se puede insertar al 'User' porque ya existe uno con el 'email': " + user.getEmail(),
						new ValidationError(USER_VALIDATION_INSERT_EMAIL_ALREADY_EXISTS, user.getEmail())));
	}
	
	public User validateUpdate(UserDto user) throws ValidationException {
		notNull(user,
				new ValidationException("No se puede modificar el 'User' porque vale 'null'",
					new ValidationError(USER_VALIDATION_UPDATE_NULL_OBJECT_VALUE)));
		
		validateFields(user);
		
		final User userBd = userQueryService.findById(user.getId()).orElse(null);
		ifTrueThrows(() -> userBd == null,
				new ValidationException("No se puede modificar el 'User' porque no existe ninguno con id:" + user.getId() + " en la B.D.",
						new ValidationError(USER_VALIDATION_UPDATE_ID_NOT_EXISTS, user.getId())));
		
		if(!StringUtils.equals(user.getName(), userBd.getName())){
			ifTrueThrows(() -> userQueryService.findByName(user.getName()).isPresent(),
					new ValidationException("No se puede modificar el campo 'name' del 'User' con 'id': " + user.getId() + " porque ya existe en la .B.D. uno con el 'name': " + user.getName(),
							new ValidationError(USER_VALIDATION_UPDATE_NAME_ALREADY_EXISTS, user.getName())));
		}
		
		if(!StringUtils.equals(user.getEmail(), userBd.getEmail())){
			ifTrueThrows(() -> userQueryService.findByEmail(user.getEmail()).isPresent(),
					new ValidationException("No se puede modificar el campo 'email' del 'User' con 'id': " + user.getId() + " porque ya existe en la .B.D. uno con el 'email': " + user.getEmail(),
							new ValidationError(USER_VALIDATION_UPDATE_EMAIL_ALREADY_EXISTS, user.getEmail())));
		}
		
		return userBd;
	}
	
	/**
	 * Validaciones de los campos del Usuario.
	 * @param user
	 * @throws ValidationException
	 */
	private void validateFields(UserDto user) throws ValidationException {
		//Id:
		mustToBeNull(user.getId(),
				new ValidationException("No se puede insertar el 'User' porque su 'id' no vale 'null'.",
					new ValidationError(USER_VALIDATION_NOTNULL_FIELD, new Object[] {UserFieldNames.ID})));
		
		//Name:
		notNull(user.getName(),
				new ValidationException("No se puede insertar el 'User' porque el campo 'name' vale 'null'",
						new ValidationError(USER_VALIDATION_NULL_FIELD, new Object[] {UserFieldNames.NAME})));
		
		maxLength(user.getName(), User.NAME_MAX_LENGHT,
				new ValidationException("No se puede insertar el 'User' porque el campo 'name' es muy largo",
						new ValidationError(USER_VALIDATION_MAXLENGTH_FIELD, new Object[] {UserFieldNames.NAME, user.getName()})));
		
		//Password:
		notNull(user.getPassword(),
				new ValidationException("No se puede insertar el 'User' porque el campo 'password' vale 'null'",
						new ValidationError(USER_VALIDATION_NULL_FIELD, new Object[] {UserFieldNames.PASSWORD})));
		
		maxLength(user.getPassword(), User.PASSWORD_MAX_LENGHT,
				new ValidationException("No se puede insertar el 'User' porque el campo 'password' es muy largo",
						new ValidationError(USER_VALIDATION_MAXLENGTH_FIELD, new Object[] {UserFieldNames.PASSWORD})));

		//Email:
		notNull(user.getEmail(),
				new ValidationException("No se puede insertar el 'User' porque el campo 'email' vale 'null'",
						new ValidationError(USER_VALIDATION_NULL_FIELD, new Object[] {UserFieldNames.EMAIL})));
		
		maxLength(user.getEmail(), User.EMAIL_MAX_LENGHT,
				new ValidationException("No se puede insertar el 'User' porque el campo 'email' es muy largo",
						new ValidationError(USER_VALIDATION_MAXLENGTH_FIELD, new Object[] {UserFieldNames.EMAIL, user.getEmail()})));
		
		isEmail(user.getEmail(),
				new ValidationException("No se puede insertar el 'User' porque el campo 'email' tiene un formato incorrecto",
						new ValidationError(USER_VALIDATION_MAXLENGTH_FIELD, new Object[] {UserFieldNames.EMAIL, user.getEmail()})));
		
		//RealName:
		maxLength(user.getRealName(), User.REAL_NAME_MAX_LENGHT,
				new ValidationException("No se puede insertar el 'User' porque el campo 'realName' es muy largo",
						new ValidationError(USER_VALIDATION_MAXLENGTH_FIELD, new Object[] {UserFieldNames.REAL_NAME})));
	}

	public static class UserFieldNames {
		private UserFieldNames () {
			//Empty constructor.
		}
		
		public static final String ID = "id";
		public static final String NAME = "name";
		public static final String PASSWORD = "password";
		public static final String EMAIL = "email";
		public static final String REAL_NAME = "realName";
	}
}