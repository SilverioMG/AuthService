package net.atopecode.authservice.model.user.converter;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.atopecode.authservice.converter.AbstractConverterMappeableNormalizable;
import net.atopecode.authservice.model.role.Role;
import net.atopecode.authservice.model.role.converter.RoleToRoleDtoConverter;
import net.atopecode.authservice.model.role.dto.RoleDto;
import net.atopecode.authservice.model.user.User;
import net.atopecode.authservice.model.user.dto.UserDto;
import net.atopecode.authservice.service.role.IRoleService;

@Component
public class UserToUserDtoConverter extends AbstractConverterMappeableNormalizable<User, UserDto>{

	private IRoleService roleService;
	private RoleToRoleDtoConverter roleToRoleDtoConverter;
	
	@Autowired
	protected UserToUserDtoConverter(ModelMapper mapper,
			IRoleService roleService,
			RoleToRoleDtoConverter roleToRoleDtoConverter) {
		super(mapper, UserDto.class);
		this.roleService = roleService;
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
		
		List<Role> roles = roleService.findRolesByUser(user);
		roles.forEach((Role role) -> {
			RoleDto roleDto = roleToRoleDtoConverter.convert(role);
			dto.getRoles().add(roleDto);
		});
	}
	
}
