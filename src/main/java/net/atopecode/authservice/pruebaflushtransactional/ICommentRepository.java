package net.atopecode.authservice.pruebaflushtransactional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ICommentRepository extends JpaRepository<Comment, Long> {
}
