package net.atopecode.authservice.model.user.converter;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.atopecode.authservice.model.user.User;
import net.atopecode.authservice.model.user.converter.base.ConverterMappeable;
import net.atopecode.authservice.model.user.dto.UserDto;

@Component
public class UserDtoToUserConverter extends ConverterMappeable<UserDto, User>{
	
	@Autowired
	public UserDtoToUserConverter(ModelMapper mapper) {
		super(mapper);
	}
	
	@Override
	public User convert(UserDto source) {
		if(source == null) {
			return null;
		}
		
		User user = mapper.map(source, User.class);
		return user;
	}
	

}
