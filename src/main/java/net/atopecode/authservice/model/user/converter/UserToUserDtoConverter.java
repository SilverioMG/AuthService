package net.atopecode.authservice.model.user.converter;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.atopecode.authservice.model.user.User;
import net.atopecode.authservice.model.user.converter.base.ConverterMappeableNormalizable;
import net.atopecode.authservice.model.user.dto.UserDto;

@Component
public class UserToUserDtoConverter extends ConverterMappeableNormalizable<User, UserDto>{

	@Autowired
	protected UserToUserDtoConverter(ModelMapper mapper) {
		super(mapper, UserDto.class);
	}
	
	

}
