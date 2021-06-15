package net.atopecode.authservice.validators.base;

import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.util.StringUtils;

import net.atopecode.authservice.validators.exception.ValidationException;

public abstract class AbstractValidator {
	
	public static final String EMAIL_REGEX = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
	
	//Fields Validations:
	public <T> void notNull(T input, ValidationException ex) throws ValidationException {
		if(input == null) {
			throw ex;
		}
	}
	
	public <T> void mustToBeNull(T input, ValidationException ex) throws ValidationException {
		if(input != null) {
			throw ex;
		}
	}
	
	public void notEmpty(String field, ValidationException ex) throws ValidationException {
		if(!StringUtils.hasText(field)) {
			throw ex;
		}
	}
	
	public void maxLength(String field, int maxLength, ValidationException ex) throws ValidationException {
		if(field == null) {
			return;
		}
		
		if(field.length() > maxLength) {
			throw ex;
		}
	}
	
	public void minLength(String field, int minLength, ValidationException ex) throws ValidationException {
		if(field == null) {
			return;
		}
		
		if(field.length() < minLength) {
			throw ex;
		}
	}
	
	@SuppressWarnings("unchecked")
	public <T> void maxValue(Comparable<T> field, Comparable<T> maxValue, ValidationException ex) throws ValidationException {
		if((field == null) || (maxValue == null)) {
			return;
		}
		
		if(field.compareTo((T)maxValue) > 0) {
			throw ex;
		}
	}
	
	@SuppressWarnings("unchecked")
	public <T> void minValue(Comparable<T> field, Comparable<T> minValue, ValidationException ex) throws ValidationException {
		if((field == null) || (minValue == null)) {
			return;
		}
		
		if(field.compareTo((T)minValue) < 0) {
			throw ex;
		}
	}
	
	@SuppressWarnings("unchecked")
	private <T> boolean checkEqualsValue(Comparable<T> field, Comparable<T> equalsValue){
		if((field == null) || (equalsValue == null)) {
			return false;
		}
		
		return (field.compareTo((T)equalsValue) == 0);
	}
	
	public <T> void equalsValue(Comparable<T> field, Comparable<T> equalsValue, ValidationException ex) throws ValidationException {
		if(!checkEqualsValue(field, equalsValue)) {
			throw ex;
		}
	}
	
	public <T> void notEqualsValue(Comparable<T> field, Comparable<T> equalsValue, ValidationException ex) throws ValidationException {
		if(checkEqualsValue(field, equalsValue)) {
			throw ex;
		}
	}	
	
	private <T> boolean checkValueIn(T field, List<T> values) {
		if((field == null) || (values == null) || (values.isEmpty())) {
			return false;
			
		}
		
		return values.contains(field);
	}
	
	public <T> void valueIn(T field, List<T> values, ValidationException ex) throws ValidationException {
		if(!checkValueIn(field, values)) {
			throw ex;
		}
	}
	
	public <T> void valueNotIn(T field, List<T> values, ValidationException ex) throws ValidationException {
		if(checkValueIn(field, values)) {
			throw ex;
		}
	}
	
	public void isEmail(String email, ValidationException ex) throws ValidationException {
		Pattern pattern = Pattern.compile(EMAIL_REGEX);
		Matcher matcher = pattern.matcher(email);
		if(!matcher.matches()) {
			throw ex;
		}
	}
	
	public void mustToBeTrue(boolean value, ValidationException ex) throws ValidationException {
		if(!value) {
			throw ex;
		}
	}
	
	public void mustToBeFalse(boolean value, ValidationException ex) throws ValidationException {
		if(value) {
			throw ex;
		}
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
