package net.atopecode.authservice.service.role;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import net.atopecode.authservice.model.role.Role;
import net.atopecode.authservice.model.role.dto.RoleDto;
import net.atopecode.authservice.validators.exception.ValidationException;

public interface IRoleService {

	public Role save(RoleDto role) throws ValidationException;
	
	public Role insert(RoleDto role) throws ValidationException;
	
	public Role update(RoleDto role) throws ValidationException;
	
	public void delete(Long idRole);
	
	//UserQueryService Methods:
	public Optional<Role> findById(Long id);
	
	//public Optional<Role> findByIdWithPermissions(Long id); //TODO...
	
	public Optional<Role> findByName(String name);
	
	//public Optional<Role> findByNameWithPermissions(String name); //TODO...
	
	public Page<Role> findAll(PageRequest pageRequest);
}
