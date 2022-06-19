package net.atopecode.authservice.validation;

import org.slf4j.Logger;

import net.atopecode.authservice.localization.messagelocalized.MessageLocalized;
import net.atopecode.authservice.validation.exceptions.ValidationBundleException;
import net.atopecode.authservice.validation.exceptions.ValidationException;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNull;

@Deprecated
public class BundleValidations {
    private Logger logger;

    private List<MessageLocalized> errors;

    public BundleValidations(){
        this.logger = null;
        this.errors = new ArrayList<>();
    }

    public BundleValidations(Logger logger){
        this();
        this.logger = logger;
    }

    public BundleValidations addError(MessageLocalized errorMessage){
        requireNonNull(errorMessage);
        errors.add(errorMessage);
        return this;
    }

    public void log(ValidationException ex){
        if(logger != null){
            logger.info(ex.getMessage());
        }
    }

    public void validate(String loggerValidationBundleErrorMessage, MessageLocalized validationBundleErrorMessage) {
        if(!errors.isEmpty()){
            throw new ValidationBundleException(loggerValidationBundleErrorMessage, validationBundleErrorMessage, errors);
        }
    }
}
