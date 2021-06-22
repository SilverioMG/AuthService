package net.atopecode.authservice.model.user.converter.base;

import org.modelmapper.ModelMapper;
import org.springframework.core.convert.converter.Converter;

import net.atopecode.authservice.model.interfaces.INormalizable;

public abstract class ConverterMappeable<S, T> implements Converter<S, T> {

	protected ModelMapper mapper;
	
	protected ConverterMappeable(ModelMapper mapper) {
		this.mapper = mapper;
	}
	
	public void map(S source, T result) {
		if((source == null) || (result == null)) {
			return;
		}
		
		this.mapper.map(source, result);
		
		if(result instanceof INormalizable) {
			INormalizable normalizable = (INormalizable) result;
			normalizable.normalize();
		}
	}
}
