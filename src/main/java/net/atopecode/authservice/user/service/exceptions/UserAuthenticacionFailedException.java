package net.atopecode.authservice.user.service.exceptions;

import net.atopecode.authservice.localization.messagelocalized.MessageLocalized;
import net.atopecode.authservice.localization.messagelocalized.exceptions.ExceptionWithLocalizedMessage;
import net.atopecode.authservice.validation.exceptions.ValidationException;
import org.springframework.http.HttpStatus;

public class UserAuthenticacionFailedException extends ValidationException {

    public static final String LOG_MESSAGE = "No se ha podido autenticar al usuario.";
    public static final String MESSAGE_CODE_USER_AUTHENTICATION_FAILED = "user.authentication.failed";

    public UserAuthenticacionFailedException(){
        super(LOG_MESSAGE, new MessageLocalized(MESSAGE_CODE_USER_AUTHENTICATION_FAILED), HttpStatus.FORBIDDEN);
    }
}
