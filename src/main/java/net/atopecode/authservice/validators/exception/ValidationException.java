package net.atopecode.authservice.validators.exception;


import net.atopecode.authservice.validators.dto.ValidationError;

public class ValidationException extends Exception {

	private static final long serialVersionUID = -495936357625618899L;
	
	public final ValidationError error;
	
	public ValidationException(String message, ValidationError error) {
		super(message);
		this.error = error;
	}
		
}
