package net.atopecode.authservice.rel_user_role.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import net.atopecode.authservice.rel_user_role.model.RelUserRole;
import net.atopecode.authservice.role.model.Role;
import net.atopecode.authservice.user.model.User;

@Repository
public interface IRelUserRoleRepository extends JpaRepository<RelUserRole, Long> {

	//Nota.- Las propiedades de navegación 'User' y 'Role' estás anotadas con 'FetchType.EAGER' por eso, sino se hace un fetch en la query, Hibernate hará
	//       una query adicional por cada propierdad para recuperarlas por separado después de obtener el registro de 'RelUserRole' correspondiente.
	
	@Query("SELECT rel from RelUserRole rel INNER JOIN FETCH rel.user u INNER JOIN FETCH rel.role WHERE (rel.user = :userParam) AND (rel.role = :roleParam)")
	Optional<RelUserRole> findByUserAndRole(@Param("userParam") User user, @Param("roleParam") Role role);
	
	@Query("SELECT rel from RelUserRole rel INNER JOIN FETCH rel.user u INNER JOIN FETCH rel.role WHERE rel.user = :userParam")
	List<RelUserRole> findByUser(@Param("userParam") User user);
}
