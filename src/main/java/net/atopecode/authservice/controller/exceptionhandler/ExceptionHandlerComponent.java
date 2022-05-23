package net.atopecode.authservice.controller.exceptionhandler;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import net.atopecode.authservice.controller.utils.ResultMessage;
import net.atopecode.authservice.localization.ILocaleService;
import net.atopecode.authservice.localization.messagelocalized.MessageLocalized;
import net.atopecode.authservice.validation.exceptions.ValidationException;


/**
 * En este Component se envía una respuesta  personalizada para todas las Exceptions no controladas que se lanzan más halla de un 'Controller' de Spring.
 * 
 * Nota.- Para los '@ExceptionHandler' de excepciones no controladas en el servidor ('RuntimeException' y 'Exception'), hay que hacer un log a mano de la Excepción para que se muestre su traza.
 * Por defecto cuando se produce una Exception y sale fuera del Controller sin que exista ningún 'ExceptionHandler' para procesarla, Spring hace un log de la traza de la Exception, pero si hay un
 * 'ExceptionHandler' tenemos que loguear nosotros la traza porque Spring deja de hacerlo. 
 * @author Silverio
 *
 */
@ControllerAdvice()
public class ExceptionHandlerComponent {
	public static final Logger LOGGER = LoggerFactory.getLogger(ExceptionHandlerComponent.class);
	
	public static final String SPANISH_LANG = "es-ES";
	
	private ILocaleService localeService;
	
	public ExceptionHandlerComponent(ILocaleService localeService) {
		this.localeService = localeService;
	}
		
	@ExceptionHandler(ValidationException.class)
	public ResponseEntity<ResultMessage<ValidationException>> validationException(ValidationException ex){
		MessageLocalized errorMessage = ex.getMessageLocalized();
		String logMessage = String.format("%s - %s", ex.getMessage(), localeService.getMessage(errorMessage, Locale.forLanguageTag(SPANISH_LANG))); 
		LOGGER.info(logMessage); 
		String localizedMessage = localeService.getMessage(errorMessage);
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
		LOGGER.error("{0}", ex);
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
		LOGGER.error("{0}", ex);
		return new ResultMessage<Exception>(ex).toResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
