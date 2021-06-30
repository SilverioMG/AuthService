package net.atopecode.authservice.service.role.query;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import net.atopecode.authservice.model.role.IRoleRepository;
import net.atopecode.authservice.model.role.Role;
import net.atopecode.authservice.util.NormalizeString;

@Service
public class RoleQueryService implements IRoleQueryService {

	private IRoleRepository roleRepository;
	
	public RoleQueryService(IRoleRepository roleRepository) {
		this.roleRepository = roleRepository;
	}
	
	public Optional<Role> findById(Long id){
		Optional<Role> role = Optional.empty();
		if(id != null) {
			role = roleRepository.findById(id);
		}
		
		return role;
	}
	
	public Optional<Role> findByName(String name){
		Optional<Role> role = Optional.empty();
		if(StringUtils.isNotBlank(name)) {
			String normalizedName = NormalizeString.normalize(name);
			role = roleRepository.findByNormalizedName(normalizedName);
		}
		
		return role;
	}
	
	public Page<Role> findAll(PageRequest pageRequest){
		Page<Role> roles = Page.empty();
		
		if(pageRequest != null) {
			roles = roleRepository.findAll(pageRequest);
		}
		
		return roles;
	}
}
