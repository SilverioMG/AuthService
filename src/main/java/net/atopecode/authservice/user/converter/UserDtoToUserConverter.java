package net.atopecode.authservice.user.converter;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.atopecode.authservice.converter.AbstractConverterMappeableNormalizable;
import net.atopecode.authservice.user.dto.UserDto;
import net.atopecode.authservice.user.model.User;


@Component
public class UserDtoToUserConverter extends AbstractConverterMappeableNormalizable<UserDto, User>{
	
	@Autowired
	public UserDtoToUserConverter(ModelMapper mapper) {
		super(mapper, User.class);
	}

}
