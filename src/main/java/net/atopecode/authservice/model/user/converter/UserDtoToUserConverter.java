package net.atopecode.authservice.model.user.converter;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.atopecode.authservice.converter.AbstractConverterMappeableNormalizable;
import net.atopecode.authservice.model.user.User;
import net.atopecode.authservice.model.user.dto.UserDto;

@Component
public class UserDtoToUserConverter extends AbstractConverterMappeableNormalizable<UserDto, User>{
	
	@Autowired
	public UserDtoToUserConverter(ModelMapper mapper) {
		super(mapper, User.class);
	}

}
