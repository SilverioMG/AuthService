package net.atopecode.authservice.role.service.query;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import net.atopecode.authservice.role.model.Role;
import net.atopecode.authservice.user.model.User;

public interface IRoleQueryService {

	public Optional<Role> findById(Long id);
	
	public Optional<Role> findByName(String name);
	
	public Page<Role> findAll(PageRequest pageRequest);
	
	public List<Role> findRolesByUser(User user);
}
