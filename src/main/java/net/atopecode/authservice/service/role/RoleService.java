package net.atopecode.authservice.service.role;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import net.atopecode.authservice.model.role.Role;
import net.atopecode.authservice.model.role.dto.RoleDto;
import net.atopecode.authservice.validators.exception.ValidationException;

@Service
public class RoleService implements IRoleService {
	public static final Logger LOGGER = LoggerFactory.getLogger(RoleService.class); 
	
	
	@Override
	public Role save(RoleDto role) throws ValidationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Role insert(RoleDto user) throws ValidationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Role update(RoleDto user) throws ValidationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(Long idRole) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Optional<Role> findById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<Role> findByName(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<Role> findAll(PageRequest pageRequest) {
		// TODO Auto-generated method stub
		return null;
	}

}
