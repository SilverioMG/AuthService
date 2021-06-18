package net.atopecode.authservice.service.user;

import java.util.Optional;

import net.atopecode.authservice.model.user.User;
import net.atopecode.authservice.model.user.dto.UserDto;

public interface IUserService {
	
	public User save(UserDto user);
	
	public User insert(UserDto user);
	
	public User update(UserDto user);
	
	public void delete(Long idUser);
	
	//UserQueryService Methods:
	public Optional<User> findById(Long id);
	
	public Optional<User> findByIdWithRoles(Long id);
	
	public Optional<User> findByName(String name);
	
	public Optional<User> findByNameWithRoles(String name);
	
	public Optional<User> findByEmail(String email);
	
	public Optional<User> findByEmailWithRoles(String email);
}
