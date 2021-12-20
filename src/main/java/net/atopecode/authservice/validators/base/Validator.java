package net.atopecode.authservice.validators.base;

import java.util.Collection;
import java.util.function.BooleanSupplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.util.StringUtils;

import net.atopecode.authservice.validators.exception.ValidationException;

public class Validator {
	
	
	public Validator() {
		//Empty Constructor.
	}
	
	
	//FIELDS VALIDATIONS:
	
	/**
	 * Devuelve 'true' si el parámetro recibido es distinto de 'null'.
	 * Devuelve 'false' si el parámetro vale 'null'. 
	 * @param <T>
	 * @param value
	 * @return
	 */
	public <T> boolean notNull(T value) {
		return (value != null);
	}
	
	/**
	 * Devuelve 'true' si el parámetro recibido vale 'null'.
	 * Devuelve 'false' si el parámetro recibido es distinto de 'null'.
	 * @param <T>
	 * @param value
	 * @return
	 */
	public <T> boolean isNull(T value) {
		return (value == null);
	}
	
	/**
	 * Laza una 'ValidationException' si el parámetro recibido vale 'null'.
	 * @param <T>
	 * @param value
	 * @param ex
	 * @throws ValidationException
	 */
	public <T> void notNull(T value, ValidationException ex) throws ValidationException {
		if(isNull(value)) {
			throw ex;
		}
	}
	
	/**
	 * Lanza una 'ValidationException' si el parámetro recibido es distinto de 'null'.
	 * @param <T>
	 * @param value
	 * @param ex
	 * @throws ValidationException
	 */
	public <T> void mustToBeNull(T value, ValidationException ex) throws ValidationException {
		if(notNull(value)) {
			throw ex;
		}
	}
	
	/**
	 * Devuelve 'true' si el parámetro recibido recibido vale 'null', cadena vacía o cadena que contiene solo espacios en blanco.
	 * Devuelve 'false' si el parámetro recibido es una cadena de texto no vacía.
	 * @param value
	 * @return
	 */
	public boolean isEmpty(String value) {
		return (!StringUtils.hasText(value));
	}
	
	/**
	 * Devuelve 'true' si el parámetro recibido es una cadena de texto no vacía.
	 * Devuelve 'false' si el parámetro recibido vale 'null', cadena vacía o cadena que contiene solo espacios en blanco.
	 * @param value
	 * @return
	 */
	public boolean notEmpty(String value) {
		return StringUtils.hasText(value);
	}
	
	/**
	 * Lanza una 'ValidationException' si el parámetro recibido vale 'null', cadena vacía o cadena que contiene solo espacios en blanco. 
	 * @param value
	 * @param ex
	 * @throws ValidationException
	 */
	public void notEmpty(String value, ValidationException ex) throws ValidationException {
		if(isEmpty(value)) {
			throw ex;
		}
	}
	
	/**
	 * Lanza una 'ValidationException' si el parámetro recibido no es una cadena vacía. 
	 * No hace nada si el parámetro recibido vale 'null', cadena vacía o cadena que contiene solo espacios en blanco. 
	 * @param value
	 * @param ex
	 * @throws ValidationException
	 */
	public void mustToBeEmpty(String value, ValidationException ex) throws ValidationException {
		if(notEmpty(value)) {
			throw ex;
		}
	}
	
	/**
	 * Devuelve 'true' si la colección recibida como parámetro está vacía.
	 * Devuelve 'false' si la colección recibida como parámetro contiene algún elemento.
	 * @param <T>
	 * @param collection
	 * @return
	 */
	public <T> boolean isEmptyCollection(Collection<T> collection){
		return (collection == null) || (collection.isEmpty()); 
	}
	
	/**
	 * Lanza una 'ValidationException' si la colección recibida como parámetro es una colección vacía.
	 * @param <T>
	 * @param collection
	 * @param ex
	 * @throws ValidationException
	 */
	public <T> void notEmptyCollection(Collection<T> collection, ValidationException ex) throws ValidationException {
		if(isEmptyCollection(collection)) {
			throw ex;
		}
	}
	
	/**
	 * Lanza una 'ValidationException' si la colección recibida como parámetro no es una colección vacía.
	 * @param <T>
	 * @param collection
	 * @param ex
	 * @throws ValidationException
	 */
	public <T> void mustToBeEmptyCollection(Collection<T> collection, ValidationException ex) throws ValidationException {
		if(!isEmptyCollection(collection)) {
			throw ex;
		}
	}
	
	/**
	 * Devuelve 'true' si el valor recibido tiene una longitud menor o igual a la indicada.
	 * Devuelve 'false'  si el valor recibido tiene una longitud mayor a la indicada.
	 * @param value
	 * @param maxLength
	 * @return
	 */
	public boolean maxLenght(String value, int maxLength) {
		if(value == null) {
			return false;
		}
		
		return (value.length() <= maxLength);
	}
	
	/**
	 * Lanza una 'ValidationException' si el valor recibido tiene una longitud mayor a la indicada.
	 * No hace nada si el valor recibido tiene una longitud menor o igual a la indicada.
	 * @param value
	 * @param maxLength
	 * @return
	 */
	public void maxLength(String value, int maxLength, ValidationException ex) throws ValidationException {
		if(!maxLenght(value, maxLength)) {
			throw ex;
		}
	}
	
	/**
	 * Devuelve 'true' si el valor recibido tiene una longitud mayor o igual a la indicada.
	 * Devuelve 'false'  si el valor recibido tiene una longitud menor a la indicada.
	 * @param value
	 * @param minLength
	 * @return
	 */
	public boolean minLenght(String value, int minLength) {
		if(value == null) {
			return false;
		}
		
		return (value.length() >= minLength);
	}

	/**
	 * Lanza una 'ValidationException' si el valor recibido tiene una longitud menor a la indicada.
	 * No hace nada si el valor recibido tiene una longitud mayor o igual a la indicada.
	 * @param value
	 * @param maxLength
	 * @return
	 */
	public void minLength(String value, int minLength, ValidationException ex) throws ValidationException {		
		if(!minLenght(value, minLength)) {
			throw ex;
		}
	}

	/**
	 * Devuelve 'true' si el valor recibido es menor o igual al indicado.
	 * Devuelve 'false' si el valor recibido es mayor al indicado.
	 * @param <T>
	 * @param value
	 * @param maxValue
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> boolean maxValue(Comparable<T> value, Comparable<T> maxValue) {
		if((value == null) || (maxValue == null)) {
			return false;
		}
		
		return (value.compareTo((T)maxValue) <= 0);
	}

	/**
	 * Lanza una 'ValidationException' si el valor recibido es mayor al indicado.
	 * No hace nada si el valor recibido es menor o igual al indicado.
	 * @param <T>
	 * @param value
	 * @param maxValue
	 * @param ex
	 * @throws ValidationException
	 */
	@SuppressWarnings("unchecked")
	public <T> void maxValue(Comparable<T> value, Comparable<T> maxValue, ValidationException ex) throws ValidationException {
		if(!maxValue(value, maxValue)) {
			throw ex;
		}
	}
	
	/**
	 * Devuelve 'true' si el valor recibido es mayor o igual al indicado.
	 * Devuelve 'false' si el valor recibido es menor al indicado.
	 * @param <T>
	 * @param value
	 * @param minValue
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> boolean minValue(Comparable<T> value, Comparable<T> minValue) {
		if((value == null) || (minValue == null)) {
			return false;
		}
		
		return (value.compareTo((T)minValue) >= 0);
	}
	
	/**
	 * Lanza una 'ValidationException' si el valor recibido es menor al indicado.
	 * No hace nada si el valor recibido es mayor o igual al indicado.
	 * @param <T>
	 * @param value
	 * @param minValue
	 * @param ex
	 * @throws ValidationException
	 */
	@SuppressWarnings("unchecked")
	public <T> void minValue(Comparable<T> value, Comparable<T> minValue, ValidationException ex) throws ValidationException {		
		if(!minValue(value, minValue)) {
			throw ex;
		}
	}
	
	/**
	 * Devuelve 'true' si los valores recibidos son iguales entre si.
	 * Devuelve 'false' si los valores recibidos son distintos entre si.
	 * @param <T>
	 * @param value
	 * @param equalsValue
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> boolean equalsValue(Comparable<T> value, Comparable<T> equalsValue){
		if((value == null) || (equalsValue == null)) {
			return false;
		}
		
		return (value.compareTo((T)equalsValue) == 0);
	}
	
	/**
	 * Lanza una 'ValidationException' si los valores recibidos son distintos entre si.
	 * No hace nada si los valores recibidos son iguales entre si.
	 * @param <T>
	 * @param value
	 * @param equalsValue
	 * @param ex
	 * @throws ValidationException
	 */
	public <T> void equalsValue(Comparable<T> value, Comparable<T> equalsValue, ValidationException ex) throws ValidationException {
		if(!equalsValue(value, equalsValue)) {
			throw ex;
		}
	}
	
	/**
	 * Lanza una 'ValidationException' si los valores recibidos son iguales entre si.
	 * No hace nada si los valores recibidos son distintos entre si.
	 * @param <T>
	 * @param value
	 * @param equalsValue
	 * @param ex
	 * @throws ValidationException
	 */
	public <T> void notEqualsValue(Comparable<T> value, Comparable<T> equalsValue, ValidationException ex) throws ValidationException {
		if(equalsValue(value, equalsValue)) {
			throw ex;
		}
	}

	/**
	 * Devuelve 'true' en caso de que el valor recibido esté incluído en la lista de valores indicados.
	 * Devuelve 'false' en caso de que el valor recibido no esté incluído en la lista de valores indicados.
	 * @param <T>
	 * @param value
	 * @param values
	 * @return
	 */
	public <T> boolean valueIn(T value, Collection<T> values) {
		if((value == null) || (values == null) || (values.isEmpty())) {
			return false;
			
		}
		
		return values.contains(value);
	}
	
	/**
	 * Lanza una 'ValidationException' si el valor recibidio no está incluído en la lista de valores indicados.
	 * No hace nada si el valor recibido está incluído en la lista de valores indicados.
	 * @param <T>
	 * @param value
	 * @param values
	 * @param ex
	 * @throws ValidationException
	 */
	public <T> void valueIn(T value, Collection<T> values, ValidationException ex) throws ValidationException {
		if(!valueIn(value, values)) {
			throw ex;
		}
	}
	
	/**
	 * Lanza una 'ValidationException' si el valor recibidio está incluído en la lista de valores indicados.
	 * No hace nada si el valor recibido no está incluído en la lista de valores indicados.
	 * @param <T>
	 * @param value
	 * @param values
	 * @param ex
	 * @throws ValidationException
	 */
	public <T> void valueNotIn(T value, Collection<T> values, ValidationException ex) throws ValidationException {
		if(valueIn(value, values)) {
			throw ex;
		}
	}
	
	/**
	 * Devuelve 'true' si el valor recibido vale 'true'.
	 * Devuelve 'false' si el valor recibido vale 'false' o 'null'.
	 * @param value
	 * @return
	 */
	public boolean isTrue(Boolean value) {
		return Boolean.TRUE.equals(value);
	}
	
	/**
	 * Lanza una 'ValidationException' si el valor recibido vale 'false' o 'null'.
	 * No hace nada si el valor recibido vale 'true'.
	 * @param value
	 * @param ex
	 * @throws ValidationException
	 */
	public void mustToBeTrue(Boolean value, ValidationException ex) throws ValidationException {
		if(!isTrue(value)) {
			throw ex;
		}
	}
	
	/**
	 * Devuelve 'true' si el valor recibido vale 'false'.
	 * Devuelve 'false' si el valor recibido vale 'true' o 'null'.
	 * @param value
	 * @return
	 */
	public boolean isFalse(Boolean value) {
		return Boolean.FALSE.equals(value);
	}
	
	/**
	 * Lanza una 'ValidationException' si el valor recibido vale 'true' o 'null'.
	 * No hace nada si el valor recibido vale 'false'.
	 * @param value
	 * @param ex
	 * @throws ValidationException
	 */
	public void mustToBeFalse(Boolean value, ValidationException ex) throws ValidationException {
		if(!isFalse(value)) {
			throw ex;
		}
	}
	
	/**
	 * Lanza una 'ValidationException' si el valor recibido no cumple con el formato de la expresión regular indicada.
	 * No hace nada si el valor recibido cumple con el formato de la expresión regular indicada.
	 * @param value
	 * @param regexPattern
	 * @param ex
	 * @throws ValidationException
	 */
	public void hasFormat(String value, String regexPattern, ValidationException ex) throws ValidationException {
		Pattern pattern = Pattern.compile(regexPattern);
		Matcher matcher = pattern.matcher(value);
		if(!matcher.matches()) {
			throw ex;
		}
	}
	
	//LOGIC VALIDATIONS:
	
	/**
	 * Lanza una 'ValidationException' si la expresión lambda recibida como parámetro devuelve 'true'.
	 * No hace nada si la expresión lambda recibida como parámetro devuelve 'false' o 'null'.
	 * @param supplier
	 * @param ex
	 * @throws ValidationException
	 */
	public void ifTrueThrows(BooleanSupplier supplier, ValidationException ex) throws ValidationException {
		if(supplier == null) {
			return;
		}
		
		if(Boolean.TRUE.equals(supplier.getAsBoolean())) {
			throw ex;
		}
	}
	
	/**
	 * Lanza una 'ValidationException' si la expresión lambda recibida como parámetro devuelve 'false'.
	 * No hace nada si la expresión lambda recibida como parámetro devuelve 'true' o 'null'.
	 * @param supplier
	 * @param ex
	 * @throws ValidationException
	 */
	public void ifFalseThrows(BooleanSupplier supplier, ValidationException ex) throws ValidationException {
		if(supplier == null) {
			return;
		}
		
		if(Boolean.FALSE.equals(supplier.getAsBoolean())) {
			throw ex;
		}
	}
}
