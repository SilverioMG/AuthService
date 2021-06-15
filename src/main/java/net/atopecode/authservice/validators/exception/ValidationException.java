package net.atopecode.authservice.validators.exception;


import net.atopecode.authservice.validators.dto.ValidationError;

public class ValidationException extends Exception {

	public final ValidationError error;
	
	public ValidationException(String message, ValidationError error) {
		super(message);
		this.error = error;
	}
		
}
