package net.atopecode.authservice.controller.utils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import net.atopecode.authservice.localization.MessageLocalized;

/**
 * Este clase se utiliza para devolver un 'ResponseEntity<ResultMessage<T>>' en los 'Controllers' para además de devolver
 * un objeto poder añadir un mensaje en el body Json de la respuesta.
 * @param <T>
 */
public class ResultMessage<T> {

    public final T result;
    public final String message;
    public final boolean success;

    public ResultMessage(){
    	result = null;
        message = "";
        success = true;
    }

    public ResultMessage(T result, String message){
        this.result = result;
        this.message = message;
        this.success = true;
    }

    public ResultMessage(T result, String message, boolean success){
        this.result = result;
        this.message = message;
        this.success = success;
    }

    public ResultMessage(String message, boolean success){
        this.result = null;
        this.message = message;
        this.success = success;
    }

    public ResultMessage(HttpStatus httpStatus, boolean success){
        this.result = null;
        this.message = httpStatus.toString();
        this.success = success;
    }

    public ResultMessage(T result, Exception ex){
        this.result = result;
        this.message = ex.getMessage();
        this.success = false;
    }

    public ResultMessage(Exception ex){
        this.result = null;
        this.message = ex.getMessage();
        this.success = false;
    }

    public ResponseEntity<ResultMessage<T>> toResponseEntity(HttpStatus httpStatus){
        return new ResponseEntity<ResultMessage<T>>(this, httpStatus);
    }
}
