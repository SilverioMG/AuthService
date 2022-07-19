package net.atopecode.authservice.validation.exceptions;

import org.springframework.http.HttpStatus;

import net.atopecode.authservice.localization.messagelocalized.MessageLocalized;
import net.atopecode.authservice.localization.messagelocalized.exceptions.RuntimeExceptionWithLocalizedMessage;

public class ValidationException extends RuntimeExceptionWithLocalizedMessage {

	public ValidationException(String loggerMessage, MessageLocalized errorMessage) {
		super(loggerMessage, errorMessage, HttpStatus.BAD_REQUEST);
	}

	public ValidationException(String loggerMessage, MessageLocalized errorMessage, HttpStatus httpStatus) {
		super(loggerMessage, errorMessage, httpStatus);
	}

}
