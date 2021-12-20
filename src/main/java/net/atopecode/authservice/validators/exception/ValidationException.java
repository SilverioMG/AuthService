package net.atopecode.authservice.validators.exception;


import net.atopecode.authservice.localization.MessageLocalized;
import net.atopecode.authservice.validators.base.ValidationGeneralMessagesCode;

public class ValidationException extends Exception {

	private static final long serialVersionUID = -495936357625618899L;
	
	/**
	 * Mensaje para devolver al cliente web traducido según el Locale recibido en los Headers de la petición Http.
	 */
	private MessageLocalized errorMessage;
	
	
	public ValidationException(String messageLog) {
		this(messageLog, new MessageLocalized());
	}
	
	public ValidationException(String loggerMessage, MessageLocalized errorMessage) {
		super(loggerMessage);
		this.errorMessage = errorMessage;
	}
	
	
	public MessageLocalized getErrorMessage() {
		return errorMessage;
	}
	
	//Métodos para asginar el 'errorMessage' según el tipo de Validación realizada:
	public ValidationException forNotNull(String fieldName) {
		errorMessage = new MessageLocalized(ValidationGeneralMessagesCode.NOT_NULL, fieldName);
		return this;
	}
	
	public ValidationException forMustToBeNull(String fieldName) {
		errorMessage = new MessageLocalized(ValidationGeneralMessagesCode.MUST_TO_BE_NULL, fieldName);
		return this;
	}

	public ValidationException forNotEmpty(String fieldName) {
		errorMessage = new MessageLocalized(ValidationGeneralMessagesCode.NOT_EMPTY, fieldName);
		return this;
	}

	public ValidationException forMustToBeEmpty(String fieldName) {
		errorMessage = new MessageLocalized(ValidationGeneralMessagesCode.MUST_TO_BE_EMPTY, fieldName);
		return this;
	}
	
	public ValidationException forNotEmptyCollection(String fieldName) {
		errorMessage = new MessageLocalized(ValidationGeneralMessagesCode.NOT_EMPTY_COLLECTION, fieldName);
		return this;
	}
	
	public ValidationException forMustToBeEmptyCollection(String fieldName) {
		errorMessage = new MessageLocalized(ValidationGeneralMessagesCode.MUST_TO_BE_EMPTY_COLLECTION, fieldName);
		return this;
	}
	
	public ValidationException forMaxLength(String fieldName, String fieldValue, int maxLength) {
		errorMessage = new MessageLocalized(ValidationGeneralMessagesCode.MAX_LENGTH, fieldName, fieldValue, maxLength);
		return this;
	}
	
	public ValidationException forMinLength(String fieldName, String fieldValue, int minLength) {
		errorMessage = new MessageLocalized(ValidationGeneralMessagesCode.MIN_LENGTH, fieldName, fieldValue, minLength);
		return this;
	}
	
	public ValidationException forMaxValue(String fieldName, String fieldValue, String maxValue) {
		errorMessage = new MessageLocalized(ValidationGeneralMessagesCode.MAX_VALUE, fieldName, fieldValue, maxValue);
		return this;
	}
	
	public ValidationException forMinValue(String fieldName, String fieldValue, String minValue) {
		errorMessage = new MessageLocalized(ValidationGeneralMessagesCode.MIN_VALUE, fieldName, fieldValue, minValue);
		return this;
	}
	
	public ValidationException forEqualsValue(String fieldName, String fieldValue, String equalsValue) {
		errorMessage = new MessageLocalized(ValidationGeneralMessagesCode.EQUALS_VALUE, fieldName, fieldValue, equalsValue);
		return this;
	}
	
	public ValidationException forNotEqualsValue(String fieldName, String fieldValue, String equalsValue) {
		errorMessage = new MessageLocalized(ValidationGeneralMessagesCode.NOT_EQUALS_VALUE, fieldName, fieldValue, equalsValue);
		return this;
	}
	
	public ValidationException forValueIn(String fieldName, String fieldValue, String values) {
		errorMessage = new MessageLocalized(ValidationGeneralMessagesCode.VALUE_IN, fieldName, fieldValue, values);
		return this;
	}
	
	public ValidationException forValueNotIn(String fieldName, String fieldValue, String values) {
		errorMessage = new MessageLocalized(ValidationGeneralMessagesCode.VALUE_NOT_IN, fieldName, fieldValue, values);
		return this;
	}
	
	public ValidationException forMustToBeTrue(String fieldName) {
		errorMessage = new MessageLocalized(ValidationGeneralMessagesCode.MUST_TO_BE_TRUE, fieldName);
		return this;
	}
	
	public ValidationException forMustToBeFalse(String fieldName) {
		errorMessage = new MessageLocalized(ValidationGeneralMessagesCode.MUST_TO_BE_FALSE, fieldName);
		return this;
	}
	
	public ValidationException forHasFormat(String fieldName, String fieldValue) {
		errorMessage = new MessageLocalized(ValidationGeneralMessagesCode.HAS_FORMAT, fieldName, fieldValue);
		return this;
	}
	
	public ValidationException forNotNullValue(String valueName) {
		errorMessage = new MessageLocalized(ValidationGeneralMessagesCode.NOT_NULL_VALUE, valueName);
		return this;
	}
	
	public ValidationException forMustToBeNullValue(String valueName) {
		errorMessage = new MessageLocalized(ValidationGeneralMessagesCode.MUST_TO_BE_NULL_VALUE, valueName);
		return this;
	}
}
