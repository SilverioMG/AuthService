package net.atopecode.authservice.validators.exception;

import net.atopecode.authservice.localization.MessageLocalized;

public class ValidationException extends Exception {

	private static final long serialVersionUID = -495936357625618899L;
	
	public final MessageLocalized errorMessage;
	
	public ValidationException(String messageLog, MessageLocalized errorMessage) {
		super(messageLog);
		this.errorMessage = errorMessage;
	}
		
}
