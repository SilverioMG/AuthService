package net.atopecode.authservice.localization;

import java.util.Locale;

import net.atopecode.authservice.localization.messagelocalized.MessageLocalized;

public interface ILocaleService {

	public String getMessage(String messageCode, Locale locale, Object... messageParams);
	
	public String getMessage(MessageLocalized messageLocalized);
	
	public String getMessage(MessageLocalized messageLocalized, Locale locale);
	
	public Locale getLocale();
}
