package net.atopecode.authservice.role.service.validator;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import net.atopecode.authservice.localization.MessageLocalized;
import net.atopecode.authservice.role.dto.RoleDto;
import net.atopecode.authservice.role.model.Role;
import net.atopecode.authservice.role.model.RoleFieldNames;
import net.atopecode.authservice.role.service.query.IRoleQueryService;
import net.atopecode.authservice.validators.base.Validator;
import net.atopecode.authservice.validators.exception.ValidationException;

@Component
public class RoleValidatorComponent extends Validator {
	
	//Códigos de archivos locale 'messages.properties' para traducción de los mensajes de error en las validaciones del 'Usuario'.
	public static final String ROLE_VALIDATION_INSERT_ID_ALREADY_EXISTS = "role.validation.insert.id.already.exists";
	public static final String ROLE_VALIDATION_INSERT_NAME_ALREADY_EXISTS = "role.validation.insert.name.already.exists";
	public static final String ROLE_VALIDATION_UPDATE_ID_NOT_EXISTS = "role.validation.update.id.not.exists";
	public static final String ROLE_VALIDATION_UPDATE_NAME_ALREADY_EXISTS = "role.validation.update.name.already.exists";
	
	private IRoleQueryService roleQueryService;
	
	public RoleValidatorComponent(IRoleQueryService roleQueryService) {
		super();
		this.roleQueryService = roleQueryService;
	}

	public void validateInsertDto(RoleDto role) throws ValidationException {
		notNull(role,
				new RoleValidationException("No se puede insertar el 'Role' porque vale 'null'")
					.forNotNullValue(RoleFieldNames.ENTITY));
		
		mustToBeNull(role.getId(),
				new RoleValidationException("No se puede insertar el 'Role' porque su campo '" + RoleFieldNames.ID + "' no vale 'null'")
					.forMustToBeNull(RoleFieldNames.ID));
		
		validateFieldsDto(role);
		
		ifTrueThrows(() -> roleQueryService.findById(role.getId()).isPresent(),
				new RoleValidationException("No se puede insertar al 'Role' porque ya existe uno con el '" + RoleFieldNames.ID + "': " + role.getId(),
						new MessageLocalized(ROLE_VALIDATION_INSERT_ID_ALREADY_EXISTS, role.getId())));
		
		ifTrueThrows(() -> roleQueryService.findByName(role.getName()).isPresent(),
				new RoleValidationException("No se puede insertar al 'Role' porque ya existe uno con el '" + RoleFieldNames.NAME + "': " + role.getName(),
						new MessageLocalized(ROLE_VALIDATION_INSERT_NAME_ALREADY_EXISTS, role.getName())));
	}

	public Role validateUpdateDto(RoleDto role) throws ValidationException {
		notNull(role,
				new ValidationException("No se puede modificar el 'Role' porque vale 'null'")
					.forNotNullValue(RoleFieldNames.ENTITY));

		notNull(role.getId(),
				new ValidationException("No se puede modificar el 'Role' porque su '" + RoleFieldNames.ID + "' vale 'null'.")
					.forNotNull(RoleFieldNames.ID));
		
		validateFieldsDto(role);
		
		final Role roleBd = roleQueryService.findById(role.getId()).orElse(null);
		ifTrueThrows(() -> roleBd == null,
				new RoleValidationException("No se puede modificar el 'Role' porque no existe ninguno con id:" + role.getId() + " en la B.D.",
						new MessageLocalized(ROLE_VALIDATION_UPDATE_ID_NOT_EXISTS, role.getId())));
		
		if(!StringUtils.equals(role.getName(), role.getName())){
			ifTrueThrows(() -> roleQueryService.findByName(role.getName()).isPresent(),
					new RoleValidationException("No se puede modificar el campo 'name' del 'Role' con 'id': " + role.getId() + " porque ya existe en la .B.D. uno con el 'name': " + role.getName(),
							new MessageLocalized(ROLE_VALIDATION_UPDATE_NAME_ALREADY_EXISTS, role.getId(), role.getName())));
		}
		
		return roleBd;
	}

	public void validateFieldsDto(RoleDto role) throws ValidationException {
		//Name:
		notEmpty(role.getName(),
				new RoleValidationException("No se puede guardar el 'Role' porque el campo '" + RoleFieldNames.NAME + "' no tiene valor.")
					.forNotEmpty(RoleFieldNames.NAME));
		
		String roleName = role.getName();
		maxLength(roleName, Role.NAME_MAX_LENGTH,
				new RoleValidationException("No se puede guardar el 'Role' porque el campo '" + RoleFieldNames.NAME + "' es muy largo")
					.forMaxLength(RoleFieldNames.NAME, roleName, Role.NAME_MAX_LENGTH));
	}

	
	public static class RoleValidationException extends ValidationException {
		
		public RoleValidationException(String logMessage) {
			super(logMessage);
		}
		
		public RoleValidationException(String logMessage, MessageLocalized errorMessage) {
			super(logMessage, errorMessage);
		}
	}
}
