package net.atopecode.authservice.model.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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
		   "where user.nm_email = :email")
	public Optional<User> findByNormalizedEmailWithRoles(@Param("email") String email);
}
