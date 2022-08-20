package net.atopecode.authservice.pruebaflushtransactional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IPostRepository extends JpaRepository<Post, Long> {
}
