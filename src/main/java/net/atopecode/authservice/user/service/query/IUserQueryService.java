package net.atopecode.authservice.user.service.query;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import net.atopecode.authservice.user.model.User;


public interface IUserQueryService {
	
	public Optional<User> findById(Long id);
	
	public Optional<User> findByIdWithRoles(Long id);
	
	public Optional<User> findByName(String name);
	
	public Optional<User> findByNameWithRoles(String name);
	
	public Optional<User> findByEmail(String email);
	
	public Optional<User> findByEmailWithRoles(String email);
	
	public Page<User> findAll(PageRequest pageRequest);
	
	public Page<User> findAllWithRoles(PageRequest pageRequest);
}