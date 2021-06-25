package net.atopecode.authservice.localization;

import java.util.Locale;

public interface ILocaleService {

	public String getMessage(String messageCode, Locale locale, Object... messageParams);
	
	public String getMessage(MessageLocalized messageLocalized);
	
	public Locale getLocale();
}
