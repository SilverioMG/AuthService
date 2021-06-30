package net.atopecode.authservice.model.role;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IRoleRepository extends JpaRepository<Role, Long>{

	@Query("SELECT role FROM Role role where role.nm_name = :name")
	public Optional<Role> findByNormalizedName(@Param("name") String normalizedName);
}
