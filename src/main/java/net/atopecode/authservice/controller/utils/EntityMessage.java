package net.atopecode.authservice.controller.utils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import net.atopecode.authservice.localization.MessageLocalized;

/**
 * Este clase se utiliza para devolver un 'ResponseEntity<EntityMessage<T>>' en los 'Controllers' para además de devolver
 * un objeto poder añadir un mensaje en el body Json de la respuesta.
 * @param <T>
 */
public class EntityMessage<T> {

    public final T entity;
    public final String message;
    public final boolean success;

    public EntityMessage(){
        entity = null;
        message = "";
        success = true;
    }

    public EntityMessage(T entity, String message){
        this.entity = entity;
        this.message = message;
        this.success = true;
    }

    public EntityMessage(T entity, String message, boolean success){
        this.entity = entity;
        this.message = message;
        this.success = success;
    }

    public EntityMessage(String message, boolean success){
        this.entity = null;
        this.message = message;
        this.success = success;
    }

    public EntityMessage(HttpStatus httpStatus, boolean success){
        this.entity = null;
        this.message = httpStatus.toString();
        this.success = success;
    }

    public EntityMessage(T entity, Exception ex){
        this.entity = entity;
        this.message = ex.getMessage();
        this.success = false;
    }

    public EntityMessage(Exception ex){
        this.entity = null;
        this.message = ex.getMessage();
        this.success = false;
    }

    public ResponseEntity<EntityMessage<T>> toResponseEntity(HttpStatus httpStatus){
        return new ResponseEntity<EntityMessage<T>>(this, httpStatus);
    }
}
