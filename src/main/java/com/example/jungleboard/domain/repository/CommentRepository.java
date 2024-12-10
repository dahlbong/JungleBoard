package com.example.jungleboard.domain.repository;

import com.example.jungleboard.domain.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    List<CommentEntity> findAllByAuthor_UserIdOrderByCreatedAtDesc(Long userId);

    void deleteAllByPost_PostId(Long postId);

    // 게시글별 댓글 조회도 필요할 수 있습니다
    List<CommentEntity> findAllByPost_PostIdOrderByCreatedAtDesc(Long postId);
}