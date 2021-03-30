package net.atopecode.authservice.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.atopecode.authservice.dao.model.User;

@Repository
interface IUserRepository extends JpaRepository<User, Long>{

}
