package net.atopecode.authservice.model.role;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface IRoleRepository extends JpaRepository<Role, Long>{

}
