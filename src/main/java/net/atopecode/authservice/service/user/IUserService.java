package net.atopecode.authservice.service.user;

import net.atopecode.authservice.model.user.User;
import net.atopecode.authservice.model.user.UserDto;

public interface IUserService {

	public User findById(Long id);
	
	public User findByIdWithRoles(Long id);
	
	public User findByName(String name);
	
	public User findByNameWithRoles(String name);
	
	public User findByEmail(String email);
	
	public User findByEmailWithRoles(String email);
	
	public User save(UserDto user);
	
	public User insert(User user);
	
	public User update(User user);
	
}
