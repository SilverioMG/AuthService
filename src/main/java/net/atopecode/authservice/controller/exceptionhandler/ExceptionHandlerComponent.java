package net.atopecode.authservice.controller.exceptionhandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import net.atopecode.authservice.controller.utils.EntityMessage;
import net.atopecode.authservice.validators.exception.ValidationException;

/**
 * En este Component se envía una respuesta  personalizada para todas las Exceptions no controladas que se lanzan más halla de un 'Controller' de Spring.
 * @author Silverio
 *
 */
@ControllerAdvice
public class ExceptionHandlerComponent {
	public static final Logger LOGGER = LoggerFactory.getLogger(ExceptionHandlerComponent.class);
	
	public ExceptionHandlerComponent() {
		//Empty Constructor.
	}
	
	@ExceptionHandler(ValidationException.class)
	public ResponseEntity<EntityMessage<ValidationException>> validationException(ValidationException ex){
		LOGGER.info(ex.getMessage());
		String localizedMessage = ex.errorMessage.getMessageCode(); //TODO... Traducir el mensaje al Locale actual con los parámetros.
		return new EntityMessage<ValidationException>(localizedMessage, false).toResponseEntity(HttpStatus.BAD_REQUEST);
	}
	
	/**
	 * Controla cualquier 'Exception' no indicada anteriormente por algún método con '@ExceptionHandler'.
	 * @param ex
	 * @return
	 */
	@ExceptionHandler(Exception.class)
	public ResponseEntity<EntityMessage<Exception>> exception(Exception ex){
		LOGGER.error("EXCEPTION NO CONTROLADA por el ServicioWeb: {}", ex.getMessage());
		return new EntityMessage<Exception>(ex).toResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
