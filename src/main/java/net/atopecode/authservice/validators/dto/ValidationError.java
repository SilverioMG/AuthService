package net.atopecode.authservice.validators.dto;

import java.util.Arrays;

public class ValidationError {
	
	private final String messageCode;
	
	private final Object[] messageParams;
	
	public ValidationError(String messageCode) {
		this(messageCode, new Object[0]);
	}
	
	public ValidationError(String messageCode, Object[] messageParams) {
		this.messageCode = messageCode;
		this.messageParams = messageParams;
	}

	public String getMessageCode() {
		return messageCode;
	}

	public Object[] getMessageParams() {
		return messageParams;
	}

	@Override
	public String toString() {
		return "ErrorDto [messageCode=" + messageCode + ", messageParams=" + Arrays.toString(messageParams) + "]";
	}
		
}
