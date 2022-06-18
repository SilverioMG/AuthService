package net.atopecode.authservice.role.service.exceptions;

import org.springframework.http.HttpStatus;

import net.atopecode.authservice.localization.messagelocalized.MessageLocalized;
import net.atopecode.authservice.localization.messagelocalized.exceptions.ExceptionWithLocalizedMessage;

public class RoleNotFoundException extends ExceptionWithLocalizedMessage {
	
	public static final String LOG_MESSAGE = "No se ha encontrado el registro de 'Rol'.";
	public static final String MESSAGE_CODE_ROLE_NOT_FOUND = "role.not.found";
	private static final MessageLocalized messageLocalized = new MessageLocalized(MESSAGE_CODE_ROLE_NOT_FOUND);
	
	
	public RoleNotFoundException() {
		super(LOG_MESSAGE, messageLocalized, HttpStatus.BAD_REQUEST);
	}

}
