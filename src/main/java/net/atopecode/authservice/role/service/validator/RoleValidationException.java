package net.atopecode.authservice.role.service.validator;

import net.atopecode.authservice.localization.messagelocalized.MessageLocalized;
import net.atopecode.authservice.validation.exceptions.ValidationException;

public class RoleValidationException extends ValidationException {
	
	public RoleValidationException(String logMessage, MessageLocalized errorMessage) {
		super(logMessage, errorMessage);
	}
}
