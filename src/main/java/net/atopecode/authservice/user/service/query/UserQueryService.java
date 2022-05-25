package net.atopecode.authservice.user.service.query;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import net.atopecode.authservice.rel_user_role.model.RelUserRole;
import net.atopecode.authservice.rel_user_role.model.RelUserRoleFieldNames;
import net.atopecode.authservice.role.model.Role;
import net.atopecode.authservice.role.model.RoleFieldNames;
import net.atopecode.authservice.service.query.AbstractQueryService;
import net.atopecode.authservice.user.dto.filter.UserFilter;
import net.atopecode.authservice.user.dto.filter.UserRoleFilter;
import net.atopecode.authservice.user.model.User;
import net.atopecode.authservice.user.model.UserFieldNames;
import net.atopecode.authservice.user.repository.IUserRepository;
import net.atopecode.authservice.util.NormalizeString;
import net.atopecode.authservice.validation.exceptions.ValidationException;

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
		if(StringUtils.isNotBlank(name)) {
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
	public Page<User> query(UserFilter filter) throws ValidationException{
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
			
			query.distinct(true);
			
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
			
			UserRoleFilter rolesFilter = filter.getRoles();
			if(rolesFilter != null) {
				UserRoleFilter.LogicComparation logic = (rolesFilter.getLogicComparation() != null) ? rolesFilter.getLogicComparation() : UserRoleFilter.LogicComparation.DEFAULT;
				switch(logic) {
					case DEFAULT:
					case OR:
						predicateRoles = joinRolesPredicate(root, rolesFilter);
						break;
					
					case AND:
						predicateRoles = subQueryRolesPredicate(root, query, builder, rolesFilter);
						break;
				}		
			}
			
			Predicate[] predicates = new Predicate[] { predicateId, predicateName, predicateEmail, predicateRealName, predicateRoles };
			predicate = super.composeNullablePredicates(builder, PredicateLogicComparation.AND, predicates);
			
			return predicate;
		};
	}
	
	/**
	 * Se hace un Join con los 'Roles' del 'User' para comprobar si pertenece a alguno (lógica OR) de los nombres de 'Role' recibidos en el filtro de la query.
	 * @param userRoot
	 * @param rolesFilter
	 * @return
	 */
	private Predicate joinRolesPredicate(Root<User> userRoot, UserRoleFilter rolesFilter) {
		Predicate predicateRoles = null;
		if((rolesFilter == null) || (ArrayUtils.isEmpty(rolesFilter.getNames()))) {
			return predicateRoles;
		}
			
		Join<User, RelUserRole> joinRelUserRoles = userRoot.join(UserFieldNames.REL_USER_ROLE);
		Join<RelUserRole, Role> joinRoles = joinRelUserRoles.join(RelUserRoleFieldNames.ROLE);
		
		String[] roles = rolesFilter.getNames();
		List<String> normalizedRolesList = Arrays.asList(NormalizeString.normalize(roles));
		predicateRoles = joinRoles.get(RoleFieldNames.NM_NAME).in(normalizedRolesList);
				
		return predicateRoles;
	}
	
	/**
	 * Se hace una SubQuery sobre la tabla de 'Roles' del 'user' para comprobar si pertenece a todos (lógica AND) los nombres de 'Role' recibidos en el filtro de la query. 
	 * @param userRoot
	 * @param rolesFilter
	 * @return
	 */
	private Predicate subQueryRolesPredicate(Root<User> userRoot, CriteriaQuery<?> query, CriteriaBuilder builder, UserRoleFilter rolesFilter) {
		Predicate predicate = null;
		if((rolesFilter == null) || (ArrayUtils.isEmpty(rolesFilter.getNames()))) {
			return predicate;
		}
		
		//SubQuery que devuelve un 'Long' (count). El nº de Roles de cada Usuario filtrando por aquellos Roles cuyos nombres coincide con los del filtro. 
		Subquery<Long> subQuery = query.subquery(Long.class);
		
		//Inner Joins con Roles de los Usuarios.
		Root<RelUserRole> rootRelUserRole = subQuery.from(RelUserRole.class);
		Join<RelUserRole, Role> joinRoles = rootRelUserRole.join(RelUserRoleFieldNames.ROLE);
		
		//select count(roles). Nº de Roles que tiene asignado cada Usuario.
		subQuery.select(builder.count(joinRoles));
		
		//where(relUserRole.idUser = user.id).
		//Condición para seleccionar los roles del cada Usuario de la Query en la SubQuery. Sino se seleccionarian para cada Usuario los Roles de todos los Usuarios en la SubQuery.
		Predicate predicateUser = builder.equal(rootRelUserRole.get(RelUserRoleFieldNames.USER), userRoot);
		
		//where(role.nm_name in ("roleNames"...)).
		//Condición para seleccionar de entre todos los Roles del Usuario solo los que se indican en el filtro.
		String[] roles = rolesFilter.getNames();
		List<String> normalizedRolesList = Arrays.asList(NormalizeString.normalize(roles));
		Predicate predicateRoles = joinRoles.get(RoleFieldNames.NM_NAME).in(normalizedRolesList);
		
		//SubQuery condition:
		//where((relUserRole.idUser = user.id) and (role.nm_name in ("roleNames"...)))
		subQuery.where(builder.and(predicateUser, predicateRoles));
		
		//Query condition:
		//La Subquery debe tener el mismo nº de filas (roles) que los nombres de 'Role' del filtro para saber que el 'User' tiene asignados todos los 'Roles'.
		//Si tiene menos roles quiere decir que algun Role del filtro no está asignado al Usuario y no se cumpliría la condición. Solo se seleccionan los Usuarios que tienen todos (AND) los
		//Roles del filtro.
		Long totalRoleNames = Long.valueOf(roles.length);
		predicate = builder.equal(subQuery, totalRoleNames);
		
		return predicate;
	}

}
