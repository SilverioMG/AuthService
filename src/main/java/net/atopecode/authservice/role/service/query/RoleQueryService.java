package net.atopecode.authservice.role.service.query;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import net.atopecode.authservice.rel_user_role.model.RelUserRole;
import net.atopecode.authservice.rel_user_role.repository.IRelUserRoleRepository;
import net.atopecode.authservice.role.model.Role;
import net.atopecode.authservice.role.repository.IRoleRepository;
import net.atopecode.authservice.user.model.User;
import net.atopecode.authservice.util.NormalizeString;

@Service
public class RoleQueryService implements IRoleQueryService {

	private IRoleRepository roleRepository;
	private IRelUserRoleRepository relUserRoleRepository;
	
	public RoleQueryService(IRoleRepository roleRepository,
			IRelUserRoleRepository relUserRoleRepository) {
		this.roleRepository = roleRepository;
		this.relUserRoleRepository = relUserRoleRepository;
	}
	
	@Override
	public Optional<Role> findById(Long id){
		Optional<Role> role = Optional.empty();
		if(id != null) {
			role = roleRepository.findById(id);
		}
		
		return role;
	}
	
	@Override
	public Optional<Role> findByName(String name){
		Optional<Role> role = Optional.empty();
		if(StringUtils.isNotBlank(name)) {
			String normalizedName = NormalizeString.normalize(name);
			role = roleRepository.findByNormalizedName(normalizedName);
		}
		
		return role;
	}
	
	@Override
	public Page<Role> findAll(PageRequest pageRequest){
		Page<Role> roles = Page.empty();
		
		if(pageRequest != null) {
			roles = roleRepository.findAll(pageRequest);
		}
		
		return roles;
	}
	
	@Override
	public List<Role> findRolesByUser(User user){
		List<RelUserRole> relUserRoles = relUserRoleRepository.findByUser(user);
		List<Role> roles = relUserRoles.stream()
			.map(RelUserRole::getRole)
			.collect(Collectors.toList());
		
		return roles;
	}
}
