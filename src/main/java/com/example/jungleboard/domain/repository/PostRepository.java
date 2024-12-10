package com.example.jungleboard.domain.repository;

import com.example.jungleboard.domain.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<PostEntity, Long> {
    List<PostEntity> findAllByAuthor_UserIdOrderByCreatedAtDesc(Long userId);
    List<PostEntity> findAllByOrderByCreatedAtDesc();
}
