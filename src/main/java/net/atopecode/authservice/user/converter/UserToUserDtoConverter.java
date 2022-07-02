package net.atopecode.authservice.user.converter;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.atopecode.authservice.converter.AbstractConverterMappeableNormalizable;
import net.atopecode.authservice.role.converter.RoleToRoleDtoConverter;
import net.atopecode.authservice.role.dto.RoleDto;
import net.atopecode.authservice.role.model.Role;
import net.atopecode.authservice.user.dto.UserDto;
import net.atopecode.authservice.user.model.User;
import net.atopecode.authservice.user.service.IUserService;

@Component
public class UserToUserDtoConverter extends AbstractConverterMappeableNormalizable<User, UserDto> {

	private IUserService userService;
	private RoleToRoleDtoConverter roleToRoleDtoConverter;
	
	@Autowired
	protected UserToUserDtoConverter(ModelMapper mapper,
			IUserService userService,
			RoleToRoleDtoConverter roleToRoleDtoConverter) {
		super(mapper, UserDto.class);
		this.userService = userService;
		this.roleToRoleDtoConverter = roleToRoleDtoConverter;
	}
	
	@Override
	public UserDto convert(User user) {
		UserDto dto = super.convert(user);
		mapRoles(user, dto);
		
		//Se elimina el 'password' cuando se envía la info hacia la web.
		dto.setPassword(null);
		
		
		return dto;
	}
	
	public UserDto convertWithPassword(User user) {
		String password = user.getPassword();
		
		UserDto userDto = this.convert(user);
		userDto.setPassword(password);
		
		return userDto;
	}

	private void mapRoles(User user, UserDto dto) {
		if((user == null) || (dto == null)) {
			return;
		}
		
		List<Role> roles = userService.getRoles(user);
		roles.forEach((Role role) -> {
			RoleDto roleDto = roleToRoleDtoConverter.convert(role);
			dto.getRoles().add(roleDto);
		});
	}

	
}
