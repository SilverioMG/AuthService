package net.atopecode.authservice.service.user;

import java.util.Optional;

import net.atopecode.authservice.model.user.User;
import net.atopecode.authservice.model.user.UserDto;

public interface IUserService {

	public Optional<User> findById(Long id);
	
	public Optional<User> findByIdWithRoles(Long id);
	
	public Optional<User> findByName(String name);
	
	public Optional<User> findByNameWithRoles(String name);
	
	public Optional<User> findByEmail(String email);
	
	public Optional<User> findByEmailWithRoles(String email);
	
	public User save(UserDto user);
	
	
}
