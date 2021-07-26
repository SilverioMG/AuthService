package net.atopecode.authservice.user.service.query;

import java.util.List;
import java.util.Optional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import net.atopecode.authservice.rel_user_role.model.RelUserRole;
import net.atopecode.authservice.rel_user_role.model.RelUserRoleFieldNames;
import net.atopecode.authservice.role.model.Role;
import net.atopecode.authservice.service.query.AbstractQueryService;
import net.atopecode.authservice.user.dto.UserFilter;
import net.atopecode.authservice.user.model.User;
import net.atopecode.authservice.user.model.UserFieldNames;
import net.atopecode.authservice.user.repository.IUserRepository;
import net.atopecode.authservice.util.NormalizeString;

@Service
public class UserQueryService extends AbstractQueryService<User, UserFilter> implements IUserQueryService{

	private IUserRepository userRepository;
	
	public UserQueryService(IUserRepository userRepository) {
		super(userRepository);
		this.userRepository = userRepository;
	}
	
	@Override
	public Optional<User> findById(Long id) {
		Optional<User> user = Optional.empty();
		if(id != null) {
			user = userRepository.findById(id);
		}
		
		return user;
	}

	@Override
	public Optional<User> findByIdWithRoles(Long id) {
		Optional<User> user = Optional.empty();
		if(id != null) {
			user = userRepository.findByIdWithRoles(id);
		}
		
		return user;
	}

	@Override
	public Optional<User> findByName(String name) {
		Optional<User> user = Optional.empty();
		if(StringUtils.hasText(name)) {
			name = NormalizeString.normalize(name);
			user = userRepository.findByNormalizedName(name);
		}
		
		return user;
	}

	@Override
	public Optional<User> findByNameWithRoles(String name) {
		Optional<User> user = Optional.empty();
		if(StringUtils.isNotBlank(name)) {
			name = NormalizeString.normalize(name);
			user = userRepository.findByNormalizedNameWithRoles(name);
		}
		
		return user;
	}

	@Override
	public Optional<User> findByEmail(String email) {
		Optional<User> user = Optional.empty();
		if(StringUtils.isNotBlank(email)) {
			email = NormalizeString.normalize(email);
			user = userRepository.findByNormalizedEmail(email);
		}
		
		return user;
	}

	@Override
	public Optional<User> findByEmailWithRoles(String email) {
		Optional<User> user = Optional.empty();
		if(StringUtils.isNotBlank(email)) {
			email = NormalizeString.normalize(email);
			user = userRepository.findByNormalizedEmailWithRoles(email);
		}
		
		return user;
	}

	@Override
	public Page<User> findAll(PageRequest pageRequest) {
		Page<User> result = Page.empty();
		if(pageRequest!= null) {
			result = userRepository.findAll(pageRequest);
		}
		
		return result;
	}
	
	@Override
	public Page<User> findAllWithRoles(PageRequest pageRequest) {
		Page<User> result = Page.empty();
		if(pageRequest != null) {
			result = userRepository.findAllWithRoles(pageRequest);
		}
		
		return result;
	}
	
	@Override
	public List<User> query(UserFilter filter){
		return super.query(filter);
	}

	@Override
	protected Specification<User> getFilterSpecification(UserFilter filter) {
		return (Root<User> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
			Predicate predicate = null;
			Predicate predicateId = null;
			Predicate predicateName = null;
			Predicate predicateEmail = null;
			Predicate predicateRealName = null;
			Predicate predicateRoles = null;
			
			if(filter == null) {
				return predicate;
			}
			
			if(filter.getId() != null) {
				predicateId = builder.equal(root.get(UserFieldNames.ID), filter.getId());
			}
			
			if(StringUtils.isNotBlank(filter.getName())) {
				String normalizedName = NormalizeString.normalize(filter.getName());
				predicateName = builder.equal(root.get(UserFieldNames.NM_NAME), normalizedName);
			}
			
			if(StringUtils.isNotBlank(filter.getEmail())) {
				String normalizedEmail = NormalizeString.normalize(filter.getEmail());
				predicateEmail = builder.equal(root.get(UserFieldNames.NM_EMAIL), normalizedEmail);
			}
			
			if(StringUtils.isNotBlank(filter.getRealName())) {
				String normalizedRealName = NormalizeString.normalize(filter.getRealName());
				predicateRealName = builder.equal(root.get(UserFieldNames.NM_REAL_NAME), normalizedRealName);
			}
			
			String[] roles = filter.getRoles();
			if(ArrayUtils.isNotEmpty(roles)) {
				predicateRoles = joinRolesPredicate(root, query, builder, roles);
			}
			
			Predicate[] predicates = new Predicate[] { predicateId, predicateName, predicateEmail, predicateRealName, predicateRoles };
			predicate = super.composeNullablePredicates(builder, PredicateLogicComparation.AND, predicates);
			
			return predicate;
		};
	}
	
	private Predicate joinRolesPredicate(Root<User> userRoot, CriteriaQuery<?> query, CriteriaBuilder builder, String[] roles) {
		Predicate predicateRoles = null;
		if(roles == null) {
			return predicateRoles;
		}
		
		Join<User, RelUserRole> joinRelUserRoles = userRoot.join(UserFieldNames.REL_USER_ROLE);
		Join<RelUserRole, Role> joinRoles = joinRelUserRoles.join(RelUserRoleFieldNames.ROLE);
		
		//TODO...
		
		
	}

}
