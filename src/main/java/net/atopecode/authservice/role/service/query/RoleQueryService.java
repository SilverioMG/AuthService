package net.atopecode.authservice.role.service.query;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import net.atopecode.authservice.dto.PageRequestDto;
import net.atopecode.authservice.rel_user_role.model.RelUserRole;
import net.atopecode.authservice.rel_user_role.repository.IRelUserRoleRepository;
import net.atopecode.authservice.role.dto.RoleFilter;
import net.atopecode.authservice.role.model.Role;
import net.atopecode.authservice.role.model.RoleFieldNames;
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
	public List<Role> query(RoleFilter filter) {
		List<Role> result = new ArrayList<>();
		Specification<Role> specification = getFilterSpecification(filter);
		PageRequest pageRequest = getFilterPageRequest(filter);
		
		if(pageRequest == null) {
			//Si 'specification' vale 'null' se hará la query sin cláusula 'where'.
			result = roleRepository.findAll(specification);
		}
		else {
			//Si 'specification' vale 'null' se hará la query sin cláusula 'where'.
			Page<Role> pageResult = roleRepository.findAll(specification, pageRequest);
			result = pageResult.getContent();
		}
		
		return result;
	}
	
	protected Specification<Role>  getFilterSpecification(RoleFilter filter) {
		return (Root<Role> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
			Predicate predicate = null;
			Predicate predicateId = null;
			Predicate predicateName = null;
			
			if(filter == null) {
				return predicate;
			}
			
			if(filter.getId() != null) {
				predicateId = builder.equal(root.get(RoleFieldNames.ID), filter.getId());
			}
			
			if(StringUtils.isNotBlank(filter.getName())) {
				String normalizedName = NormalizeString.normalize(filter.getName());
				predicateName = builder.equal(root.get(RoleFieldNames.NM_NAME), normalizedName);
			}
			
			//Se concatenan los predicados que no valgan 'null' con una condición de tipo 'and':
			Predicate[] predicates = new Predicate[] {predicateId, predicateName };
			for(Predicate p: predicates) {
				if(p != null) {
					predicate = (predicate == null) ? p : builder.and(predicate, p);
				}
			}
			
			return predicate;
		};
	}
	
	protected PageRequest getFilterPageRequest(RoleFilter filter) {
		PageRequest pageRequest = null;
		PageRequestDto pageRequestDto = filter.getPageRequest();
		if(pageRequestDto != null) {
			
			pageRequest = PageRequest.of(pageRequestDto.getPageNumber(), pageRequestDto.getPageSize(), 
					Sort.Direction.fromOptionalString(pageRequestDto.getOrderSort().toString()).orElse(Sort.Direction.ASC), 
					pageRequestDto.getSortFieldName());
		}
		
		return pageRequest;
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
