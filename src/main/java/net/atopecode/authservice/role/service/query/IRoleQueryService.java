package net.atopecode.authservice.role.service.query;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import net.atopecode.authservice.role.dto.RoleFilter;
import net.atopecode.authservice.role.model.Role;
import net.atopecode.authservice.user.model.User;

public interface IRoleQueryService {

	public Optional<Role> findById(Long id);
	
	public Optional<Role> findByName(String name);

	public List<Role> findAll();

	public Page<Role> findAll(PageRequest pageRequest);
	
	public Page<Role>  query(RoleFilter filter);
	
	public List<Role> findRolesByUser(User user);
}
