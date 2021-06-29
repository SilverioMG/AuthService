package net.atopecode.authservice.validators.base;

import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.util.StringUtils;

import net.atopecode.authservice.localization.MessageLocalized;
import net.atopecode.authservice.validators.exception.ValidationException;

public abstract class AbstractValidator<TEntity> {
	
	private Class<TEntity> classEntity;
	
	//Codes of Locale .properties files:
	public static final String VALIDATION_NOT_NULL_FIELD = "validation.not.null.field";
	public static final String VALIDATION_NULL_FIELD = "validation.null.field";
	public static final String VALIDATION_MAXLENGTH_FIELD = "validation.maxlength.field";
	public static final String VALIDATION_FORMAT_FIELD = "validation.format.field";
	public static final String VALIDATION_NOT_EMPTY_FIELD = "validation.not.empty.field";
	public static final String VALIDATION_MUST_TO_BE_EMPTY_FIELD = "validation.must.to.be.empty.field";
	public static final String VALIDATION_MINLENGTH_FIELD = "validation.minlength.field";
	public static final String VALIDATION_MAX_VALUE = "validation.max.value";
	public static final String VALIDATION_MIN_VALUE = "validation.min.value";
	public static final String VALIDATION_EQUALS_VALUE = "validation.equals.value";
	public static final String VALIDATION_NOT_EQUALS_VALUE = "validation.not.equals.value";
	public static final String VALIDATION_VALUE_IN = "validation.value.in";
	public static final String VALIDATION_VALUE_NOT_IN = "validation.value.not.in";
	public static final String VALIDATION_MUST_TO_BE_TRUE ="validation.must.to.be.true";
	public static final String VALIDATION_MUST_TO_BE_FALSE ="validation.must.to.be.false";
	
	protected AbstractValidator(Class<TEntity> classEntity) {
		this.classEntity = classEntity;
	}
	
	public String getEntityName() {
		return classEntity.getName();
	}
	
	//Fields Validations:
	public <T> void notNull(T value, ValidationException ex) throws ValidationException {
		if(value == null) {
			throw ex;
		}
	}
	
	public <T> void notNull(T value, String messageLog, String fieldName) throws ValidationException {
		notNull(value,
				new ValidationException(messageLog, new MessageLocalized(VALIDATION_NOT_NULL_FIELD, fieldName, getEntityName())));
	}
	
	public <T> void mustToBeNull(T value, ValidationException ex) throws ValidationException {
		if(value != null) {
			throw ex;
		}
	}
	
	public <T> void mustToBeNull(T value, String messageLog, String fieldName) throws ValidationException {
		mustToBeNull(value,
				new ValidationException(messageLog, new MessageLocalized(VALIDATION_NULL_FIELD, fieldName, getEntityName())));
	}
	
	public void notEmpty(String value, ValidationException ex) throws ValidationException {
		if(!StringUtils.hasText(value)) {
			throw ex;
		}
	}
	
	public void notEmpty(String value, String messageLog, String fieldName) throws ValidationException{
		notEmpty(value,
				new ValidationException(messageLog, new MessageLocalized(VALIDATION_NOT_EMPTY_FIELD, fieldName, getEntityName())));
	}
	
	
	public void mustToBeEmpty(String value, ValidationException ex) throws ValidationException {
		if(StringUtils.hasText(value)) {
			throw ex;
		}
	}
	
	public void mustToBeEmpty(String value, String messageLog, String fieldName) throws ValidationException{
		mustToBeEmpty(value,
				new ValidationException(messageLog, new MessageLocalized(VALIDATION_MUST_TO_BE_EMPTY_FIELD, fieldName, getEntityName())));
	}
	
	
	
	public void maxLength(String value, int maxLength, ValidationException ex) throws ValidationException {
		if(value == null) {
			return;
		}
		
		if(value.length() > maxLength) {
			throw ex;
		}
	}
	
	public void maxLength(String value, int maxLength, String messageLog, String fieldName) throws ValidationException {
		maxLength(value, maxLength,
				new ValidationException(messageLog, new MessageLocalized(VALIDATION_MAXLENGTH_FIELD, fieldName, getEntityName(), value, maxLength)));
	}
	
	public void minLength(String value, int minLength, ValidationException ex) throws ValidationException {
		if(value == null) {
			return;
		}
		
		if(value.length() < minLength) {
			throw ex;
		}
	}
	
	public void minLenght(String value, int minLength, String messageLog, String fieldName) throws ValidationException {
		minLength(value, minLength,
				new ValidationException(messageLog, new MessageLocalized(VALIDATION_MINLENGTH_FIELD, fieldName, getEntityName(), value, minLength)));
	}
	
	@SuppressWarnings("unchecked")
	public <T> void maxValue(Comparable<T> value, Comparable<T> maxValue, ValidationException ex) throws ValidationException {
		if((value == null) || (maxValue == null)) {
			return;
		}
		
		if(value.compareTo((T)maxValue) > 0) {
			throw ex;
		}
	}
	
	public <T> void maxValue(Comparable<T> value, Comparable<T> maxValue, String messageLog, String fieldName) throws ValidationException {
		maxValue(value, maxValue,
				new ValidationException(messageLog, new MessageLocalized(VALIDATION_MAX_VALUE, fieldName, getEntityName(), value, maxValue)));
	}
	
	@SuppressWarnings("unchecked")
	public <T> void minValue(Comparable<T> value, Comparable<T> minValue, ValidationException ex) throws ValidationException {
		if((value == null) || (minValue == null)) {
			return;
		}
		
		if(value.compareTo((T)minValue) < 0) {
			throw ex;
		}
	}
	
	public <T> void minValue(Comparable<T> value, Comparable<T> minValue, String messageLog, String fieldName) throws ValidationException {
		minValue(value, minValue,
				new ValidationException(messageLog, new MessageLocalized(VALIDATION_MIN_VALUE, fieldName, getEntityName(), value, minValue)));
	}
	
	@SuppressWarnings("unchecked")
	private <T> boolean checkEqualsValue(Comparable<T> value, Comparable<T> equalsValue){
		if((value == null) || (equalsValue == null)) {
			return false;
		}
		
		return (value.compareTo((T)equalsValue) == 0);
	}
	
	public <T> void equalsValue(Comparable<T> value, Comparable<T> equalsValue, ValidationException ex) throws ValidationException {
		if(!checkEqualsValue(value, equalsValue)) {
			throw ex;
		}
	}
	
	public <T> void equalsValue(Comparable<T> value, Comparable<T> equalsValue, String messageLog, String fieldName) throws ValidationException {
		equalsValue(value, equalsValue,
				new ValidationException(messageLog, new MessageLocalized(VALIDATION_EQUALS_VALUE, fieldName, getEntityName(), value, equalsValue)));
	}
	
	public <T> void notEqualsValue(Comparable<T> value, Comparable<T> equalsValue, ValidationException ex) throws ValidationException {
		if(checkEqualsValue(value, equalsValue)) {
			throw ex;
		}
	}
	
	public <T> void notEqualsValue(Comparable<T> value, Comparable<T> equalsValue, String messageLog, String fieldName) throws ValidationException {
		notEqualsValue(value, equalsValue,
				new ValidationException(messageLog, new MessageLocalized(VALIDATION_NOT_EQUALS_VALUE, fieldName, getEntityName(), value, equalsValue)));
	}
	
	private <T> boolean checkValueIn(T value, List<T> values) {
		if((value == null) || (values == null) || (values.isEmpty())) {
			return false;
			
		}
		
		return values.contains(value);
	}
	
	public <T> void valueIn(T value, List<T> values, ValidationException ex) throws ValidationException {
		if(!checkValueIn(value, values)) {
			throw ex;
		}
	}
	
	public <T> void valueIn(T value, List<T> values, String messageLog, String fieldName) throws ValidationException {
		valueIn(value, values,
				new ValidationException(messageLog, new MessageLocalized(VALIDATION_VALUE_IN, fieldName, getEntityName(), value, values)));
	}
	
	public <T> void valueNotIn(T value, List<T> values, ValidationException ex) throws ValidationException {
		if(checkValueIn(value, values)) {
			throw ex;
		}
	}
	
	public <T> void valueNotIn(T value, List<T> values, String messageLog, String fieldName) throws ValidationException {
		valueNotIn(value, values,
				new ValidationException(messageLog, new MessageLocalized(VALIDATION_VALUE_NOT_IN, fieldName, getEntityName(), value, values)));
	}
	
	public void hasFormat(String value, String regexPattern, ValidationException ex) throws ValidationException {
		Pattern pattern = Pattern.compile(regexPattern);
		Matcher matcher = pattern.matcher(value);
		if(!matcher.matches()) {
			throw ex;
		}
	}
	
	public void hasFormat(String value, String regexPattern, String messageLog, String fieldName) throws ValidationException {
		hasFormat(value, regexPattern,
				new ValidationException(messageLog, new MessageLocalized(VALIDATION_FORMAT_FIELD, fieldName, getEntityName(), value)));
	}
	
	public void mustToBeTrue(boolean value, ValidationException ex) throws ValidationException {
		if(!value) {
			throw ex;
		}
	}
	
	public void mustToBeTrue(boolean value, String messageLog, String fieldName) throws ValidationException {
		mustToBeTrue(value,
				new ValidationException(messageLog, new MessageLocalized(VALIDATION_MUST_TO_BE_TRUE, fieldName, getEntityName())));
	}
	
	public void mustToBeFalse(boolean value, ValidationException ex) throws ValidationException {
		if(value) {
			throw ex;
		}
	}
	
	public void mustToBeFalse(boolean value, String messageLog, String fieldName) throws ValidationException {
		mustToBeFalse(value,
				new ValidationException(messageLog, new MessageLocalized(VALIDATION_MUST_TO_BE_FALSE, fieldName, getEntityName())));
	}
	
	//Logic Validations:
	public void ifTrueThrows(BooleanSupplier supplier, ValidationException ex) throws ValidationException {
		if(supplier == null) {
			return;
		}
		
		if(Boolean.TRUE.equals(supplier.getAsBoolean())) {
			throw ex;
		}
	}
	
	public void ifFalseThrows(BooleanSupplier supplier, ValidationException ex) throws ValidationException {
		if(supplier == null) {
			return;
		}
		
		if(Boolean.FALSE.equals(supplier.getAsBoolean())) {
			throw ex;
		}
	}
}
