package net.atopecode.authservice.controller.exceptionhandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import net.atopecode.authservice.controller.utils.ResultMessage;
import net.atopecode.authservice.localization.ILocaleService;
import net.atopecode.authservice.validators.exception.ValidationException;

/**
 * En este Component se envía una respuesta  personalizada para todas las Exceptions no controladas que se lanzan más halla de un 'Controller' de Spring.
 * @author Silverio
 *
 */
@ControllerAdvice()
public class ExceptionHandlerComponent {
	public static final Logger LOGGER = LoggerFactory.getLogger(ExceptionHandlerComponent.class);
	
	private ILocaleService localeService;
	
	public ExceptionHandlerComponent(ILocaleService localeService) {
		this.localeService = localeService;
	}
		
	@ExceptionHandler(ValidationException.class)
	public ResponseEntity<ResultMessage<ValidationException>> validationException(ValidationException ex){
		LOGGER.info(ex.getMessage());
		String localizedMessage = localeService.getMessage(ex.errorMessage);
		return new ResultMessage<ValidationException>(localizedMessage, false).toResponseEntity(HttpStatus.BAD_REQUEST);
	}
	
	/**
	 * Controla cualquier 'RuntimeException' no indicada anteriormente por algún método con '@ExceptionHandler'.
	 * @param ex
	 * @return
	 */
	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<ResultMessage<Exception>> runtimeException(RuntimeException ex){
		LOGGER.error("RUNTIMEEXCEPTION NO CONTROLADA por el ServicioWeb: {}", ex.getMessage());
		return new ResultMessage<Exception>(ex).toResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	/**
	 * Controla cualquier 'Exception' no indicada anteriormente por algún método con '@ExceptionHandler'.
	 * @param ex
	 * @return
	 */
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ResultMessage<Exception>> exception(Exception ex){
		LOGGER.error("EXCEPTION NO CONTROLADA por el ServicioWeb: {}", ex.getMessage());
		return new ResultMessage<Exception>(ex).toResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
