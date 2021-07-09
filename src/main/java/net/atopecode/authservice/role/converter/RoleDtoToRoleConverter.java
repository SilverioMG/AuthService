package net.atopecode.authservice.role.converter;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.atopecode.authservice.converter.AbstractConverterMappeableNormalizable;
import net.atopecode.authservice.role.dto.RoleDto;
import net.atopecode.authservice.role.model.Role;

@Component
public class RoleDtoToRoleConverter extends AbstractConverterMappeableNormalizable<RoleDto, Role>{

	@Autowired
	public RoleDtoToRoleConverter(ModelMapper mapper) {
		super(mapper, Role.class);
	}

}
