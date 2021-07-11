package net.atopecode.authservice.user.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import net.atopecode.authservice.user.model.User;

@Repository
public interface IUserRepository extends JpaRepository<User, Long>{

	@Query("SELECT user from User user LEFT JOIN FETCH user.relUserRole relUserRole INNER JOIN FETCH relUserRole.role role " +
		   "WHERE user.id = :id")
	public Optional<User> findByIdWithRoles(@Param("id") Long id);
	
	@Query("SELECT user from User user WHERE user.nm_name = :name")
	public Optional<User> findByNormalizedName(@Param("name") String normalizedName);
	
	@Query("SELECT user from User user LEFT JOIN FETCH user.relUserRole relUserRole INNER JOIN FETCH relUserRole.role role " +
		   "WHERE nm_name = :name")
	public Optional<User> findByNormalizedNameWithRoles(@Param("name") String normalizedName);
	
	@Query("SELECT user from User user where user.nm_email = :email")
	public Optional<User> findByNormalizedEmail(@Param("email") String email);
	
	@Query("SELECT user from User user LEFT JOIN FETCH user.relUserRole relUserRole INNER JOIN FETCH relUserRole.role role " +
		   "WHERE user.nm_email = :email")
	public Optional<User> findByNormalizedEmailWithRoles(@Param("email") String email);
	
	/*IMPORTANTE: En esta query, al ser paginada y utilizar FETCH (se trae campos de otras tablas en el SELECT) Hibernate no tiene manera
	 * de hacer la paginación en una sola consulta y entonces trae todo el resultado de la query a memoria y luego hace la paginación teniendo
	 * en cuenta cual es la tabla/entidad root sobre la que se hace el SELECT.
	 * Puede ocasionar problemas de memoria si las tablas tienen muchos registros (la consulta si paginar es muy grande).
	 * No usar FETCH en consultas paginadas. Recuperar los registros de las propiedades de navegación que son 'LazyLoad' posteriormente desde código con otra consulta.
	 */
	@Query(value = "SELECT user from User user LEFT JOIN FETCH user.relUserRole relUserRole INNER JOIN FETCH relUserRole.role role",
		   countQuery = "SELECT COUNT(user) from User user LEFT JOIN user.relUserRole relUserRole INNER JOIN relUserRole.role role")
	public Page<User> findAllWithRoles(PageRequest pageRequest);
}
