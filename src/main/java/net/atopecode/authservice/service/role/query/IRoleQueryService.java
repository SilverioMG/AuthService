package net.atopecode.authservice.service.role.query;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import net.atopecode.authservice.model.role.Role;
import net.atopecode.authservice.model.user.User;

public interface IRoleQueryService {

	public Optional<Role> findById(Long id);
	
	public Optional<Role> findByName(String name);
	
	public Page<Role> findAll(PageRequest pageRequest);
	
	public List<Role> findRolesByUser(User user);
}
