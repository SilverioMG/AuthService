package net.atopecode.authservice.controller.utils;
import java.util.Arrays;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import net.atopecode.authservice.localization.ILocaleService;
import net.atopecode.authservice.localization.messagelocalized.MessageLocalized;


/**
 * Este clase se utiliza para devolver un 'ResponseEntity<ResultMessage<T>>' en los 'Controllers' para además de devolver
 * un objeto poder añadir un mensaje en el body Json de la respuesta.
 * @param <T>
 */
public class ResultMessage<TResult> {

    private TResult result;
    private String message;
    private boolean ok;
    private String[] errors;
    

    public ResultMessage(){
    	this.result = null;
        this.message = "";
        this.ok = true;
        this.errors = new String[0];
    }
    
    public ResultMessage(TResult result){
    	this();
        this.result = result;
    }

    public ResultMessage(TResult result, boolean ok, String message){
        this();
        this.result = result;
        this.ok = ok;
        this.message = message;
    }

    public ResultMessage(boolean ok, String message){
        this();
        this.ok = ok;
        this.message = message;
    }
    
    public ResultMessage(String message, String[] errors) {
    	this();
    	this.message = message;
    	this.ok = false;
    	this.errors = errors;
    }

    public ResultMessage(TResult result, ILocaleService localeService, MessageLocalized messageLocalized, boolean success) {
    	this.result = result;
    	this.message = localeService.getMessage(messageLocalized);
    	this.ok = success;
    }
    
    
    public TResult getResult() {
		return result;
	}

	public String getMessage() {
		return message;
	}

	public boolean isOk() {
		return ok;
	}

	public String[] getErrors() {
		return errors;
	}

	public ResponseEntity<ResultMessage<TResult>> toResponseEntity(HttpStatus httpStatus){
        return new ResponseEntity<ResultMessage<TResult>>(this, httpStatus);
    }

	@Override
	public String toString() {
		return "ResultMessage [result=" + result + ", message=" + message + ", ok=" + ok + ", errors="
				+ Arrays.toString(errors) + "]";
	}
	
}
