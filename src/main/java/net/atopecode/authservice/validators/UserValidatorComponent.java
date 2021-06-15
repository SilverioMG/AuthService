package net.atopecode.authservice.validators;


import org.springframework.stereotype.Component;

import net.atopecode.authservice.model.user.User;
import net.atopecode.authservice.validators.base.AbstractValidator;
import net.atopecode.authservice.validators.dto.ValidationError;
import net.atopecode.authservice.validators.exception.ValidationException;

@Component
public class UserValidatorComponent extends AbstractValidator {

	public static final String USER_VALIDATION_NULL_OBJECT_VALUE = "user.validation.null.object.value";
	
	public static final String USER_VALIDATION_INSERT_NOTNULL_FIELD = "user.validation.insert.notnull.field";
	public static final String USER_VALIDATION_INSERT_NULL_FIELD = "user.validation.insert.null.field";
	public static final String USER_VALIDATION_INSERT_MAXLENGTH_FIELD = "user.validation.insert.maxlength.field";
	

	public UserValidatorComponent() {
		
	}
	
	
	public void validateInsert(User user) throws ValidationException {
		validateFields(user);
		
		//TODO... Validar la lógica de negocio:
		

	}
	
	private void validateFields(User user) throws ValidationException {
		//User:
		notNull(user, 
				new ValidationException("No se puede insertar el 'User' porque vale 'null'",
					new ValidationError(USER_VALIDATION_NULL_OBJECT_VALUE, new Object[0])));
		
		//Id:
		mustToBeNull(user.getId(),
				new ValidationException("No se puede insertar el 'User' porque su 'id' no vale 'null'.",
					new ValidationError(USER_VALIDATION_INSERT_NOTNULL_FIELD, new Object[] {"id"})));
		
		//Name:
		notNull(user.getName(),
				new ValidationException("No se puede insertar el 'User' porque el campo 'name' vale 'null'",
						new ValidationError(USER_VALIDATION_INSERT_NULL_FIELD, new Object[] {"name"})));
		
		maxLength(user.getName(), User.NAME_MAX_LENGHT,
				new ValidationException("No se puede insertar el 'User' porque el campo 'name' es muy largo",
						new ValidationError(USER_VALIDATION_INSERT_MAXLENGTH_FIELD, new Object[] {"name", user.getName()})));
		
		//Password:
		notNull(user.getPassword(),
				new ValidationException("No se puede insertar el 'User' porque el campo 'password' vale 'null'",
						new ValidationError(USER_VALIDATION_INSERT_NULL_FIELD, new Object[] {"password"})));
		
		maxLength(user.getPassword(), User.PASSWORD_MAX_LENGHT,
				new ValidationException("No se puede insertar el 'User' porque el campo 'password' es muy largo",
						new ValidationError(USER_VALIDATION_INSERT_MAXLENGTH_FIELD, new Object[] {"password"})));

		//Email:
		notNull(user.getEmail(),
				new ValidationException("No se puede insertar el 'User' porque el campo 'email' vale 'null'",
						new ValidationError(USER_VALIDATION_INSERT_NULL_FIELD, new Object[] {"email"})));
		
		maxLength(user.getEmail(), User.EMAIL_MAX_LENGHT,
				new ValidationException("No se puede insertar el 'User' porque el campo 'email' es muy largo",
						new ValidationError(USER_VALIDATION_INSERT_MAXLENGTH_FIELD, new Object[] {"email", user.getEmail()})));
		
		isEmail(user.getEmail(),
				new ValidationException("No se puede insertar el 'User' porque el campo 'email' tiene un formato incorrecto",
						new ValidationError(USER_VALIDATION_INSERT_MAXLENGTH_FIELD, new Object[] {"email", user.getEmail()})));
		
		//RealName:
		maxLength(user.getRealName(), User.REAL_NAME_MAX_LENGHT,
				new ValidationException("No se puede insertar el 'User' porque el campo 'realName' es muy largo",
						new ValidationError(USER_VALIDATION_INSERT_MAXLENGTH_FIELD, new Object[] {"realName"})));
	}

}
