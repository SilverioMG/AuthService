package net.atopecode.authservice.validation.exceptions;

import org.springframework.http.HttpStatus;

import net.atopecode.authservice.localization.messagelocalized.MessageLocalized;
import net.atopecode.authservice.localization.messagelocalized.exceptions.RuntimeExceptionWithLocalizedMessage;

public class ValidationException extends RuntimeExceptionWithLocalizedMessage {

	/**
	 * Mensaje para devolver al cliente web traducido según el Locale (el locale se establece a partir de los Headers de la petición Http).
	 */
	public ValidationException(String loggerMessage, MessageLocalized errorMessage) {
		super(loggerMessage, errorMessage, HttpStatus.BAD_REQUEST);
	}

}
