package net.atopecode.authservice.model.rel_user_role;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.atopecode.authservice.model.role.Role;
import net.atopecode.authservice.model.user.User;

@Repository
public interface IRelUserRoleRepository extends JpaRepository<RelUserRole, Long> {

	Optional<RelUserRole> findByUserAndRole(User user, Role role);
	
	List<RelUserRole> findByUser(User user);
}
