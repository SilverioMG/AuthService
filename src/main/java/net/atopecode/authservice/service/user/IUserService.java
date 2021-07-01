package net.atopecode.authservice.service.user;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import net.atopecode.authservice.model.role.dto.RoleDto;
import net.atopecode.authservice.model.user.User;
import net.atopecode.authservice.model.user.dto.UserDto;
import net.atopecode.authservice.validators.exception.ValidationException;

public interface IUserService {
	
	public User save(UserDto user) throws ValidationException;
	
	public User insert(UserDto user) throws ValidationException;
	
	public User update(UserDto user) throws ValidationException;
	
	public User setRolesToUser(Long idUser, Set<RoleDto> rolesDto) throws ValidationException;
	
	public void delete(Long idUser);
	
	//UserQueryService Methods:
	public Optional<User> findById(Long id);
	
	public Optional<User> findByIdWithRoles(Long id);
	
	public Optional<User> findByName(String name);
	
	public Optional<User> findByNameWithRoles(String name);
	
	public Optional<User> findByEmail(String email);
	
	public Optional<User> findByEmailWithRoles(String email);
	
	public Page<User> findAll(PageRequest pageRequest);
}
