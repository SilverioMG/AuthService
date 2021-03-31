package net.atopecode.authservice.model.rel_user_role;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface IRelUserRoleRepository extends JpaRepository<RelUserRole, Long> {

}
