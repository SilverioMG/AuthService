package net.atopecode.authservice.role.converter;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.atopecode.authservice.converter.AbstractConverterMappeableNormalizable;
import net.atopecode.authservice.role.dto.RoleDto;
import net.atopecode.authservice.role.model.Role;

@Component
public class RoleToRoleDtoConverter extends AbstractConverterMappeableNormalizable<Role, RoleDto>{
	
	@Autowired
	public RoleToRoleDtoConverter(ModelMapper mapper) {
		super(mapper, RoleDto.class);
	}
	

}
