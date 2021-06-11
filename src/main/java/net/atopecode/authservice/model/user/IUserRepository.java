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
	
	@Query("SELECT user from User user WHERE nm_name = :name")
	public Optional<User> findByNormalizeName(@Param("name") String normalizeName);
}
