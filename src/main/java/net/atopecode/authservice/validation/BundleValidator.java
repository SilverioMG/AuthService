package net.atopecode.authservice.validation;

import java.util.Objects;
import java.util.function.Consumer;

import org.slf4j.Logger;

import net.atopecode.authservice.localization.messagelocalized.MessageLocalized;
import net.atopecode.authservice.validation.exceptions.ValidationBundleException;
import net.atopecode.authservice.validation.exceptions.ValidationException;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;

public class BundleValidator<TValidator extends Validator> {
	
	private TValidator validator;
	private Logger logger;
	private List<MessageLocalized> errors;
	

	public BundleValidator(TValidator validator) {
		requireNonNull(validator);
		this.validator = validator;
		this.logger = null;
		this.errors = new ArrayList<>();
	}

	public BundleValidator(TValidator validator, Logger logger) {
		this(validator);
		this.logger = logger;		
	}
	
	
	public BundleValidator<TValidator> bundle(Consumer<TValidator> validation){
		requireNonNull(validation);

		try{
			validation.accept(validator);
		}
		catch (ValidationException ex){
			log(ex);
			addError(ex.getMessageLocalized());
		}
		
		return this;
	}
	
    public void validate(String logBundleValidationErrorMessage, MessageLocalized bundleValidationErrorMessage) {
        if(!errors.isEmpty()){
            throw new ValidationBundleException(logBundleValidationErrorMessage, bundleValidationErrorMessage, errors);
        }
    }
	
    protected BundleValidator<TValidator> addError(MessageLocalized errorMessage){
        requireNonNull(errorMessage);
        errors.add(errorMessage);
        return this;
    }

    protected void log(ValidationException ex){
        if(logger != null){
            logger.info(ex.getMessage());
        }
    }
	
}
