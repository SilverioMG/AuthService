package net.atopecode.authservice.service.user.validator;


import org.springframework.stereotype.Component;

import net.atopecode.authservice.model.user.User;
import net.atopecode.authservice.service.user.query.IUserQueryService;
import net.atopecode.authservice.validators.base.AbstractValidator;
import net.atopecode.authservice.validators.dto.ValidationError;
import net.atopecode.authservice.validators.exception.ValidationException;

@Component
public class UserValidatorComponent extends AbstractValidator {

	public static final String USER_VALIDATION_NULL_OBJECT_VALUE = "user.validation.null.object.value";
	
	public static final String USER_VALIDATION_INSERT_NOTNULL_FIELD = "user.validation.insert.notnull.field";
	public static final String USER_VALIDATION_INSERT_NULL_FIELD = "user.validation.insert.null.field";
	public static final String USER_VALIDATION_INSERT_MAXLENGTH_FIELD = "user.validation.insert.maxlength.field";
	public static final String USER_VALIDATION_INSERT_ID_ALREADY_EXISTS = "user.validation.insert.id.already.exists";
	public static final String USER_VALIDATION_INSERT_NAME_ALREADY_EXISTS = "user.validation.insert.name.already.exists";

	private IUserQueryService userQueryService;
	
	public UserValidatorComponent(IUserQueryService userQueryService) {
		this.userQueryService = userQueryService;
	}
	
	/**
	 * Validaciones de campos y lógica de negocio a la hora de insertar un nuevo Usuario.
	 * @param user
	 * @throws ValidationException
	 */
	public void validateInsert(User user) throws ValidationException {
		notNull(user, 
				new ValidationException("No se puede insertar el 'User' porque vale 'null'",
					new ValidationError(USER_VALIDATION_NULL_OBJECT_VALUE)));
		
		validateFields(user);
		
		ifTrueThrows(() -> userQueryService.findById(user.getId()).isPresent(),
				new ValidationException("No se puede insertar al 'user' porque ya existe uno con el 'id': " + user.getId(),
						new ValidationError(USER_VALIDATION_INSERT_ID_ALREADY_EXISTS, user.getId())));
		
		ifTrueThrows(() -> userQueryService.findByName(user.getName()).isPresent(),
				new ValidationException("No se puede insertar al 'user' porque ya existe uno con el 'name': " + user.getName(),
						new ValidationError(USER_VALIDATION_INSERT_NAME_ALREADY_EXISTS, user.getName())));
	}
	
	public void validasteUpdate(User user) throws ValidationException {
		//TODO...
	}
	
	/**
	 * Validaciones de los campos del Usuario.
	 * @param user
	 * @throws ValidationException
	 */
	private void validateFields(User user) throws ValidationException {
		//Id:
		mustToBeNull(user.getId(),
				new ValidationException("No se puede insertar el 'User' porque su 'id' no vale 'null'.",
					new ValidationError(USER_VALIDATION_INSERT_NOTNULL_FIELD, new Object[] {UserFieldNames.ID})));
		
		//Name:
		notNull(user.getName(),
				new ValidationException("No se puede insertar el 'User' porque el campo 'name' vale 'null'",
						new ValidationError(USER_VALIDATION_INSERT_NULL_FIELD, new Object[] {UserFieldNames.NAME})));
		
		maxLength(user.getName(), User.NAME_MAX_LENGHT,
				new ValidationException("No se puede insertar el 'User' porque el campo 'name' es muy largo",
						new ValidationError(USER_VALIDATION_INSERT_MAXLENGTH_FIELD, new Object[] {UserFieldNames.NAME, user.getName()})));
		
		//Password:
		notNull(user.getPassword(),
				new ValidationException("No se puede insertar el 'User' porque el campo 'password' vale 'null'",
						new ValidationError(USER_VALIDATION_INSERT_NULL_FIELD, new Object[] {UserFieldNames.PASSWORD})));
		
		maxLength(user.getPassword(), User.PASSWORD_MAX_LENGHT,
				new ValidationException("No se puede insertar el 'User' porque el campo 'password' es muy largo",
						new ValidationError(USER_VALIDATION_INSERT_MAXLENGTH_FIELD, new Object[] {UserFieldNames.PASSWORD})));

		//Email:
		notNull(user.getEmail(),
				new ValidationException("No se puede insertar el 'User' porque el campo 'email' vale 'null'",
						new ValidationError(USER_VALIDATION_INSERT_NULL_FIELD, new Object[] {UserFieldNames.EMAIL})));
		
		maxLength(user.getEmail(), User.EMAIL_MAX_LENGHT,
				new ValidationException("No se puede insertar el 'User' porque el campo 'email' es muy largo",
						new ValidationError(USER_VALIDATION_INSERT_MAXLENGTH_FIELD, new Object[] {UserFieldNames.EMAIL, user.getEmail()})));
		
		isEmail(user.getEmail(),
				new ValidationException("No se puede insertar el 'User' porque el campo 'email' tiene un formato incorrecto",
						new ValidationError(USER_VALIDATION_INSERT_MAXLENGTH_FIELD, new Object[] {UserFieldNames.EMAIL, user.getEmail()})));
		
		//RealName:
		maxLength(user.getRealName(), User.REAL_NAME_MAX_LENGHT,
				new ValidationException("No se puede insertar el 'User' porque el campo 'realName' es muy largo",
						new ValidationError(USER_VALIDATION_INSERT_MAXLENGTH_FIELD, new Object[] {UserFieldNames.REAL_NAME})));
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
