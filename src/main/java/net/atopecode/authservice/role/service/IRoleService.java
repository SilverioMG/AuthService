package net.atopecode.authservice.role.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import net.atopecode.authservice.role.dto.RoleDto;
import net.atopecode.authservice.role.dto.RoleFilter;
import net.atopecode.authservice.role.model.Role;
import net.atopecode.authservice.user.model.User;
import net.atopecode.authservice.validation.exceptions.ValidationException;

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
	
	public List<Role> query(RoleFilter filter);
	
	public List<Role> findRolesByUser(User user);
}
