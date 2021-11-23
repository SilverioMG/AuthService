package net.atopecode.authservice.localization;

import java.util.Arrays;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

/**
 * Este Servicio se utiliza para traducir en diferentes idiomas los mensajes del servicio web que se devuelven al cliente que lo consume.
 * El Locale lo asigna SpringBoot automáticamente a partir del header 'Accept-Language' en cada petición Http al Servicio Web.
 * Sino se recibe el header 'Accept-Language' y el Locale vale 'null' o se indica un Locale para el que no existe archivo .properties del idioma correspondiente,
 * se utilizará automáticamente el archivo '/resources/locale/messages.properties' que
 * es el que tiene los mensajes traducidos al idioma por defecto.
 * En '/resources/locale/' están el resto de archivos .properties para la traducción a cada idioma correspondiente.
 * Ver también en el archivo 'application.properties' la configuración para 'spring.messages' que pueden modificar el funcionamiento de 'MessageResource'.
 */
@Service
public class LocaleService implements ILocaleService {

	public static final Logger LOGGER = LoggerFactory.getLogger(LocaleService.class); 
	
	private MessageSource messageSource;
	
	@Autowired
	public LocaleService(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
	
	@Override
	public String getMessage(String messageCode, Locale locale, Object... messageParams) {
		if(StringUtils.isEmpty(messageCode)) {
			throw new LocaleServiceRuntimeException("Se ha intentado traducir un mensaje en 'LocaleService' sin valor para 'messageCode'.");
		}
		
		String result = messageCode;
		
		try {			
			result = messageSource.getMessage(messageCode, messageParams, locale);
		}
		catch(Exception ex) {
			LOGGER.info("Se ha producido una Exception al intentar traducir el 'messageCode': {}, con 'locale': {} y 'params': {}", 
					messageCode, (locale != null) ? locale.toString() : "null", Arrays.toString(messageParams), ex);
		}
		
		return result;
	}

	@Override
	public String getMessage(MessageLocalized messageLocalized) {
		return getMessage(messageLocalized.getMessageCode(), getLocale(), messageLocalized.getMessageParams());
	}
	
	@Override
	public Locale getLocale() {
		return LocaleContextHolder.getLocale();
	}
		
	public static class LocaleServiceRuntimeException extends RuntimeException{
	
		private static final long serialVersionUID = -2999479942149788532L;

		public LocaleServiceRuntimeException(String message) {
			super(message);
		}
	}
}
