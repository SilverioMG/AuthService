package net.atopecode.authservice.user.converter;

import java.util.List;

import javax.persistence.Persistence;

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
public class UserToUserDtoConverter extends AbstractConverterMappeableNormalizable<User, UserDto>{

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
	public UserDto convert(User source) {
		UserDto dto = super.convert(source);
		mapRoles(source, dto);
		
		return dto;
	}
	
	public UserDto convertWithoutPassword(User user) {
		UserDto userDto = this.convert(user);
		if(userDto != null) {
			userDto.setPassword(null);
		}
		
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
