package net.atopecode.authservice.service.role;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.atopecode.authservice.model.role.IRoleRepository;
import net.atopecode.authservice.model.role.Role;
import net.atopecode.authservice.model.role.converter.RoleDtoToRoleConverter;
import net.atopecode.authservice.model.role.dto.RoleDto;
import net.atopecode.authservice.service.role.query.IRoleQueryService;
import net.atopecode.authservice.service.role.validator.RoleValidatorComponent;
import net.atopecode.authservice.validators.exception.ValidationException;

@Service
public class RoleService implements IRoleService {
	public static final Logger LOGGER = LoggerFactory.getLogger(RoleService.class); 
	
	private IRoleRepository roleRepository;
	private IRoleQueryService roleQueryService;
	private RoleValidatorComponent roleValidator;
	private RoleDtoToRoleConverter roleDtoToRoleConverter;
	
	@Autowired
	public RoleService(IRoleRepository roleRepository,
			IRoleQueryService roleQueryService,
			RoleValidatorComponent roleValidator,
			RoleDtoToRoleConverter roleDtoToRoleConverter) {
		this.roleRepository = roleRepository;
		this.roleQueryService = roleQueryService;
		this.roleValidator = roleValidator;
		this.roleDtoToRoleConverter = roleDtoToRoleConverter;
	}
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Role save(RoleDto role) throws ValidationException {
		Role result = null;
		if(role == null) {
			return result;
		}
		
		if(role.getId() == null) {
			return insert(role);
		}
		else {
			return update(role);
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Role insert(RoleDto roleDto) throws ValidationException {
		roleValidator.validateInsertDto(roleDto);
		
		Role role = roleDtoToRoleConverter.convert(roleDto);
		role = roleRepository.save(role);
		
		return role;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Role update(RoleDto roleDto) throws ValidationException {
		Role role = roleValidator.validateUpdateDto(roleDto);
		
		roleDtoToRoleConverter.map(roleDto, role);
		role = roleRepository.save(role);
		
		return role;
	}

	@Override
	public void delete(Long idRole) {
		if(idRole == null) {
			return;
		}
		
		Role role = roleQueryService.findById(idRole).orElse(null);
		if(role != null) {
			roleRepository.delete(role);
		}		
	}

	@Override
	public Optional<Role> findById(Long id) {
		return roleQueryService.findById(id);
	}

	@Override
	public Optional<Role> findByName(String name) {
		return roleQueryService.findByName(name);
	}

	@Override
	public Page<Role> findAll(PageRequest pageRequest) {
		return roleQueryService.findAll(pageRequest);
	}

}
