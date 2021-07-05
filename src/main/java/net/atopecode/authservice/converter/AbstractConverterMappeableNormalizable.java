package net.atopecode.authservice.converter;

import org.modelmapper.ModelMapper;
import org.springframework.core.convert.converter.Converter;

import net.atopecode.authservice.model.interfaces.INormalizable;

public abstract class AbstractConverterMappeableNormalizable<S, T> implements Converter<S, T> {

	protected ModelMapper mapper;
	protected Class<T> classTarget;
	
	protected AbstractConverterMappeableNormalizable(ModelMapper mapper, Class<T> classTarget) {
		this.mapper = mapper;
		this.classTarget = classTarget;
	}
	
	/**
	 * Crea un nuevo objeto de tipo '<T>' asignando los valores de los campo que tienen el mismo del objeto
	 * recibido como parámetro de tipo '<S>'.
	 * Si el objeto de tipo '<T>' implementa la interfaz 'INormalizable' se normalizan sus campos automáticamente.
	 */
	@Override
	public T convert(S source) {
		if(source == null) {
			return null;
		}
		
		T target = mapper.map(source, classTarget);
		normalizeTarget(target);
		return target;
	}
	
	/**
	 * Asigna el valor de los campos que tiene el mismo nombre en el objeto de tipo '<T>' a partir de los valores del objeto
	 * de tipo '<S>'.
	 * Si el objeto de tipo '<T>' implementa la interfaz 'INormalizable' se normalizan sus campos automáticamente.
	 * @param source
	 * @param result
	 */
	public void map(S source, T target) {
		if((source == null) || (target == null)) {
			return;
		}
		
		mapper.map(source, target);
		normalizeTarget(target);
	}
	
	protected void normalizeTarget(T target) {
		if(target instanceof INormalizable) {
			INormalizable normalizable = (INormalizable) target;
			normalizable.normalize();
		}
	}
}
