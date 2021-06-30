package net.atopecode.authservice.model.user.converter;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.atopecode.authservice.converter.AbstractConverterMappeableNormalizable;
import net.atopecode.authservice.model.user.User;
import net.atopecode.authservice.model.user.dto.UserDto;

@Component
public class UserToUserDtoConverter extends AbstractConverterMappeableNormalizable<User, UserDto>{

	@Autowired
	protected UserToUserDtoConverter(ModelMapper mapper) {
		super(mapper, UserDto.class);
	}
	
	public UserDto convertWithoutPassword(User user) {
		UserDto userDto = super.convert(user);
		if(userDto != null) {
			userDto.setPassword(null);
		}
		
		return userDto;
	}

}
