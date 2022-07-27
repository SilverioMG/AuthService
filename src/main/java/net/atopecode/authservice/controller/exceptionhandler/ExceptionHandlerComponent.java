package net.atopecode.authservice.controller.exceptionhandler;

import java.util.List;
import java.util.Locale;

import org.aspectj.bridge.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import net.atopecode.authservice.controller.utils.ResultMessage;
import net.atopecode.authservice.localization.ILocaleService;
import net.atopecode.authservice.localization.messagelocalized.MessageLocalized;
import net.atopecode.authservice.localization.messagelocalized.exceptions.ExceptionWithLocalizedMessage;
import net.atopecode.authservice.localization.messagelocalized.exceptions.RuntimeExceptionWithLocalizedMessage;
import net.atopecode.authservice.validation.exceptions.ValidationBundleException;
import net.atopecode.authservice.validation.exceptions.ValidationException;


/**
 * En este Component se envía una respuesta  personalizada para todas las Exceptions no controladas que se lanzan más halla de un 'Controller' de Spring.
 * 
 * Nota.- Para los '@ExceptionHandler' de excepciones no controladas en el servidor, hay que hacer un log a mano de la Excepción para que se muestre su traza.
 * Por defecto cuando se produce una Exception y sale fuera del Controller sin que exista ningún 'ExceptionHandler' para procesarla, Spring hace un log de la traza de la Exception, pero si hay un
 * 'ExceptionHandler' tenemos que loguear nosotros la traza porque Spring deja de hacerlo. 
 * @author Silverio
 *
 */
@ControllerAdvice()
public class ExceptionHandlerComponent {
	public static final Logger LOGGER = LoggerFactory.getLogger(ExceptionHandlerComponent.class);
	
	public static final String SPANISH_LANG = "es-ES";
	public static final String SERVER_ERROR_MESSAGE_KEY = "atopecode.backend-utils.server.error.message";
	
	private ILocaleService localeService;
	
	@Autowired
	public ExceptionHandlerComponent(ILocaleService localeService) {
		this.localeService = localeService;
	}

	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<ResultMessage<Void>> userNameOrEmailGeneratingJWTNotFoundException(BadCredentialsException ex) {
		String localizedMessage = ex.getLocalizedMessage();
		return new ResultMessage<Void>(false, localizedMessage)
				.toResponseEntity(HttpStatus.FORBIDDEN);
	}

	@ExceptionHandler(ValidationException.class)
	public ResponseEntity<ResultMessage<Void>> validationException(ValidationException ex){
		return runtimeExceptionWithLocalizedMessage(ex);
	}
	
	@ExceptionHandler(ValidationBundleException.class)
	public ResponseEntity<ResultMessage<Void>> validationBundleException(ValidationBundleException ex){
		MessageLocalized errorMessage = ex.getMessageLocalized();
		String logMessage = String.format("%s - %s",  ex.getMessage(), translate(errorMessage, Locale.forLanguageTag(SPANISH_LANG)));
		LOGGER.info(logMessage);
		String localizedMessage = translate(errorMessage);
		String[] errorMessages = translateBundle(ex.getErrors());
		return new ResultMessage<Void>(localizedMessage, errorMessages)
				.toResponseEntity(ex.getHttpStatus());
	}
	
	@ExceptionHandler(RuntimeExceptionWithLocalizedMessage.class)
	public ResponseEntity<ResultMessage<Void>> runtimeExceptionWithLocalizedMessage(RuntimeExceptionWithLocalizedMessage ex){
		MessageLocalized errorMessage = ex.getMessageLocalized();
		String logMessage = String.format("%s - %s", ex.getMessage(), translate(errorMessage, Locale.forLanguageTag(SPANISH_LANG))); 
		LOGGER.info(logMessage);
		String localizedMessage = translate(errorMessage);
		return new ResultMessage<Void>(false, localizedMessage)
				.toResponseEntity(ex.getHttpStatus());
	}
	
	@ExceptionHandler(ExceptionWithLocalizedMessage.class)
	public ResponseEntity<ResultMessage<Void>> exceptionWithLocalizedMessage(ExceptionWithLocalizedMessage ex){
		MessageLocalized errorMessage = ex.getMessageLocalized();
		String logMessage = String.format("%s - %s", ex.getMessage(), translate(errorMessage, Locale.forLanguageTag(SPANISH_LANG))); 
		LOGGER.info(logMessage);
		String localizedMessage = translate(errorMessage);
		return new ResultMessage<Void>(false, localizedMessage)
				.toResponseEntity(ex.getHttpStatus());
	}
	
	/**
	 * Controla cualquier 'RuntimeException' no indicada anteriormente por algún método con '@ExceptionHandler'.
	 * @param ex
	 * @return
	 */
	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<ResultMessage<Void>> runtimeException(RuntimeException ex){
		LOGGER.error("RUNTIMEEXCEPTION NO CONTROLADA por el ServicioWeb: {}", ex.getMessage());
		LOGGER.error("{0}", ex);
		MessageLocalized messageLocalized = new MessageLocalized(SERVER_ERROR_MESSAGE_KEY);
		String message = translate(messageLocalized);
		return new ResultMessage<Void>(false, message)
				.toResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	/**
	 * Controla cualquier 'Exception' no indicada anteriormente por algún método con '@ExceptionHandler'.
	 * @param ex
	 * @return
	 */
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ResultMessage<Void>> exception(Exception ex){
		LOGGER.error("EXCEPTION NO CONTROLADA por el ServicioWeb: {}", ex.getMessage());
		LOGGER.error("{0}", ex);
		MessageLocalized messageLocalized = new MessageLocalized(SERVER_ERROR_MESSAGE_KEY);
		String message = translate(messageLocalized);
		return new ResultMessage<Void>(false, message)
				.toResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	
	//Se devuelve el mensaje traducido según el Locale recibido en los Headers de la petición Http.
	private String translate(MessageLocalized messageLocalized) {
		return localeService.getMessage(messageLocalized);
	}
	
	//Se devuelve el mensaje traducido según el Locale recibido como parámetro.
	private String translate(MessageLocalized messageLocalized, Locale locale) {
		return localeService.getMessage(messageLocalized, locale);
	}
	
	private String[] translateBundle(List<MessageLocalized> messagesLocalized) {
		return messagesLocalized.stream()
				.map(this::translate)
				.toArray(String[]::new);
	}
}
