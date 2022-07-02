package net.atopecode.authservice.role.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import net.atopecode.authservice.role.dto.RoleDto;
import net.atopecode.authservice.role.dto.RoleFilter;
import net.atopecode.authservice.role.model.Role;
import net.atopecode.authservice.role.service.exceptions.RoleNotFoundException;
import net.atopecode.authservice.user.model.User;
import net.atopecode.authservice.validation.exceptions.ValidationException;

public interface IRoleService {

	public Role save(RoleDto role) throws ValidationException;
	
	public Role insert(RoleDto role) throws ValidationException;
	
	public Role update(RoleDto role) throws ValidationException;
	
	public void delete(Long idRole);
	
	//UserQueryService Methods:
	public Role findById(Long id) throws RoleNotFoundException;
	
	public Role findByName(String name) throws RoleNotFoundException;
	
	public Page<Role> findAll(PageRequest pageRequest);
	
	public Page<Role> query(RoleFilter filter);
	
	public List<Role> findRolesByUser(User user);
}
