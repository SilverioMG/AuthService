package net.atopecode.authservice.user.service.validator;

import net.atopecode.authservice.localization.messagelocalized.MessageLocalized;
import net.atopecode.authservice.validation.exceptions.ValidationException;

public class UserValidationException extends ValidationException {

	
	public UserValidationException(String logMessage, MessageLocalized errorMessage) {
		super(logMessage, errorMessage);
	}
}