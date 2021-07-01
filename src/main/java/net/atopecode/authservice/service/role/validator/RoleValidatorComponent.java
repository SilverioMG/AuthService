package net.atopecode.authservice.service.role.validator;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import net.atopecode.authservice.localization.MessageLocalized;
import net.atopecode.authservice.model.role.Role;
import net.atopecode.authservice.model.role.RoleFieldNames;
import net.atopecode.authservice.model.role.dto.RoleDto;
import net.atopecode.authservice.service.role.query.IRoleQueryService;
import net.atopecode.authservice.validators.base.AbstractValidator;
import net.atopecode.authservice.validators.exception.ValidationException;

@Component
public class RoleValidatorComponent extends AbstractValidator<Role, RoleDto> {
	
	//Codes of Locale .properties files:
	public static final String ROLE_VALIDATION_INSERT_NULL_OBJECT_VALUE = "role.validation.insert.null.object.value";
	public static final String ROLE_VALIDATION_INSERT_NOTNULL_ID = "role.validation.insert.notnull.id";
	public static final String ROLE_VALIDATION_INSERT_ID_ALREADY_EXISTS = "role.validation.insert.id.already.exists";
	public static final String ROLE_VALIDATION_INSERT_NAME_ALREADY_EXISTS = "role.validation.insert.name.already.exists";
	public static final String ROLE_VALIDATION_UPDATE_NULL_OBJECT_VALUE = "role.validation.update.null.object.value";
	public static final String ROLE_VALIDATION_UPDATE_NULL_ID = "role.validation.update.null.id";
	public static final String ROLE_VALIDATION_UPDATE_ID_NOT_EXISTS = "role.validation.update.id.not.exists";
	public static final String ROLE_VALIDATION_UPDATE_NAME_ALREADY_EXISTS = "role.validation.update.name.already.exists";
	
	private IRoleQueryService roleQueryService;
	
	public RoleValidatorComponent(IRoleQueryService roleQueryService) {
		super(Role.class);
		this.roleQueryService = roleQueryService;
	}

	public void validateInsertDto(RoleDto role) throws ValidationException {
		notNull(role,
				new ValidationException("No se puede insertar el 'Role' porque vale 'null'",
					new MessageLocalized(ROLE_VALIDATION_INSERT_NULL_OBJECT_VALUE)));
		
		mustToBeNull(role.getId(),
				new ValidationException("No se puede insertar el 'Role' porque su campo '" + RoleFieldNames.ID + "' no vale 'null'",
					new MessageLocalized(ROLE_VALIDATION_INSERT_NOTNULL_ID)));
		
		validateFieldsDto(role);
		
		ifTrueThrows(() -> roleQueryService.findById(role.getId()).isPresent(),
				new ValidationException("No se puede insertar al 'Role' porque ya existe uno con el '" + RoleFieldNames.ID + "': " + role.getId(),
						new MessageLocalized(ROLE_VALIDATION_INSERT_ID_ALREADY_EXISTS, role.getId())));
		
		ifTrueThrows(() -> roleQueryService.findByName(role.getName()).isPresent(),
				new ValidationException("No se puede insertar al 'Role' porque ya existe uno con el '" + RoleFieldNames.NAME + "': " + role.getName(),
						new MessageLocalized(ROLE_VALIDATION_INSERT_NAME_ALREADY_EXISTS, role.getName())));
	}

	public Role validateUpdateDto(RoleDto role) throws ValidationException {
		notNull(role,
				new ValidationException("No se puede modificar el 'Role' porque vale 'null'",
					new MessageLocalized(ROLE_VALIDATION_UPDATE_NULL_OBJECT_VALUE)));

		notNull(role.getId(),
				new ValidationException("No se puede modificar el 'Role' porque su '" + RoleFieldNames.ID + "' vale 'null'.",
					new MessageLocalized(ROLE_VALIDATION_UPDATE_NULL_ID)));
		
		validateFieldsDto(role);
		
		final Role roleBd = roleQueryService.findById(role.getId()).orElse(null);
		ifTrueThrows(() -> roleBd == null,
				new ValidationException("No se puede modificar el 'Role' porque no existe ninguno con id:" + role.getId() + " en la B.D.",
						new MessageLocalized(ROLE_VALIDATION_UPDATE_ID_NOT_EXISTS, role.getId())));
		
		if(!StringUtils.equals(role.getName(), role.getName())){
			ifTrueThrows(() -> roleQueryService.findByName(role.getName()).isPresent(),
					new ValidationException("No se puede modificar el campo 'name' del 'Role' con 'id': " + role.getId() + " porque ya existe en la .B.D. uno con el 'name': " + role.getName(),
							new MessageLocalized(ROLE_VALIDATION_UPDATE_NAME_ALREADY_EXISTS, role.getId(), role.getName())));
		}
		
		return roleBd;
	}

	public void validateFieldsDto(RoleDto role) throws ValidationException {
		//Name:
		notEmpty(role.getName(), "No se puede guardar el 'Role' porque el campo '" + RoleFieldNames.NAME + "' no tiene valor",  RoleFieldNames.NAME);
		
		maxLength(role.getName(), Role.NAME_MAX_LENGTH, "No se puede guardar el 'Role' porque el campo '" + RoleFieldNames.NAME + "' es muy largo", RoleFieldNames.NAME);
	}

}
