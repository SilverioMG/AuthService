package net.atopecode.authservice.user.service.exceptions;

import java.text.MessageFormat;

import org.springframework.http.HttpStatus;

import net.atopecode.authservice.localization.messagelocalized.MessageLocalized;
import net.atopecode.authservice.localization.messagelocalized.exceptions.ExceptionWithLocalizedMessage;
	
public class UserNotFoundException extends ExceptionWithLocalizedMessage {

	public static final String LOG_MESSAGE = "No se ha encontrado el 'Usuario': {0}";
	public static final String MESSAGE_CODE_USER_NOT_FOUND = "user.not.found";

	public UserNotFoundException(String userSearchValue) {
			super(generateLogMessage(userSearchValue), generateMessageLocalized(userSearchValue), HttpStatus.BAD_REQUEST);
	}
	
	private static String generateLogMessage(String userSearchValue) {
		return MessageFormat.format(LOG_MESSAGE, userSearchValue);
	}
	
	private static MessageLocalized generateMessageLocalized(String userSearchValue) {
		return new MessageLocalized(MESSAGE_CODE_USER_NOT_FOUND, userSearchValue);
	}

}
