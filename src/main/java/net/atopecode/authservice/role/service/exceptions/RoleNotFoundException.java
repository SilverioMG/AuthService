package net.atopecode.authservice.role.service.exceptions;

import java.text.MessageFormat;

import org.springframework.http.HttpStatus;

import net.atopecode.authservice.localization.messagelocalized.MessageLocalized;
import net.atopecode.authservice.localization.messagelocalized.exceptions.ExceptionWithLocalizedMessage;

public class RoleNotFoundException extends ExceptionWithLocalizedMessage {
	
	public static final String LOG_MESSAGE = "No se ha encontrado el 'Rol': {0}.";
	public static final String MESSAGE_CODE_ROLE_NOT_FOUND = "role.not.found";
	
	
	public RoleNotFoundException(String roleSearchValue) {
		super(generateLogMessage(roleSearchValue), generateMessageLocalized(roleSearchValue), HttpStatus.BAD_REQUEST);
	}
	
	private static String generateLogMessage(String roleSearchValue) {
		return MessageFormat.format(LOG_MESSAGE, roleSearchValue);
	}
	
	private static MessageLocalized generateMessageLocalized(String roleSearchValue) {
		return new MessageLocalized(MESSAGE_CODE_ROLE_NOT_FOUND, roleSearchValue);
	}
	
}
