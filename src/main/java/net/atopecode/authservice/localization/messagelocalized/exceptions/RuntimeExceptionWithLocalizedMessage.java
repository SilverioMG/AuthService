package net.atopecode.authservice.localization.messagelocalized.exceptions;

import org.springframework.http.HttpStatus;

import net.atopecode.authservice.localization.messagelocalized.MessageLocalized;

public class RuntimeExceptionWithLocalizedMessage extends RuntimeException {

    private final MessageLocalized messageLocalized;
    private final HttpStatus httpStatus;

    public RuntimeExceptionWithLocalizedMessage(
    		String messageLog, 
    		MessageLocalized messageLocalized, 
    		HttpStatus httpStatus){
        super(messageLog);
        this.messageLocalized = messageLocalized;
        this.httpStatus = httpStatus;
    }

    public MessageLocalized getMessageLocalized(){
        return messageLocalized;
    }
    
    public HttpStatus getHttpStatus() {
    	return httpStatus;
    }
}
