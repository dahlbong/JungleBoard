package com.example.jungleboard.service;

import com.example.jungleboard.domain.entity.CommentEntity;
import com.example.jungleboard.domain.entity.PostEntity;
import com.example.jungleboard.domain.entity.UserEntity;
import com.example.jungleboard.domain.repository.CommentRepository;
import com.example.jungleboard.domain.repository.PostRepository;
import com.example.jungleboard.domain.repository.UserRepository;
import com.example.jungleboard.dto.CommentDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {
    private static final Logger log = LoggerFactory.getLogger(CommentService.class);

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public CommentDto.Response createComment(String email, Long postId, CommentDto.CreateRequest request) {
        try {
            UserEntity user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new EntityNotFoundException("User not found"));

            PostEntity post = postRepository.findById(postId)
                    .orElseThrow(() -> new EntityNotFoundException("Post not found"));

            CommentEntity comment = CommentEntity.createComment(
                    user,
                    post,
                    request.getContent()
            );

            CommentEntity savedComment = commentRepository.save(comment);
            return CommentDto.Response.from(savedComment);
        } catch (Exception e) {
            log.error("Error creating comment: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Transactional
    public CommentDto.Response updateComment(String email, Long commentId, CommentDto.UpdateRequest request) {
        try {
            UserEntity user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new EntityNotFoundException("User not found"));

            CommentEntity comment = commentRepository.findById(commentId)
                    .orElseThrow(() -> new EntityNotFoundException("Comment not found"));

            validateCommentAuthor(comment, user.getUserId());

            comment.updateContent(request.getContent());
            return CommentDto.Response.from(comment);
        } catch (Exception e) {
            log.error("Error updating comment: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Transactional
    public void deleteComment(String email, Long commentId) {
        try {
            UserEntity user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new EntityNotFoundException("User not found"));

            CommentEntity comment = commentRepository.findById(commentId)
                    .orElseThrow(() -> new EntityNotFoundException("Comment not found"));

            validateCommentAuthor(comment, user.getUserId());

            commentRepository.delete(comment);
        } catch (Exception e) {
            log.error("Error deleting comment: {}", e.getMessage(), e);
            throw e;
        }
    }

    public List<CommentDto.Response> getPostComments(Long postId) {
        try {
            return commentRepository.findAllByPost_PostIdOrderByCreatedAtDesc(postId)
                    .stream()
                    .map(CommentDto.Response::from)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error getting post comments: {}", e.getMessage(), e);
            throw e;
        }
    }

    public List<CommentDto.Response> getUserCommentsByEmail(String email) {
        try {
            UserEntity user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new EntityNotFoundException("User not found"));
            return getUserCommentsByUserId(user.getUserId());
        } catch (Exception e) {
            log.error("Error getting user comments by email: {}", e.getMessage(), e);
            throw e;
        }
    }

    public List<CommentDto.Response> getUserCommentsByUserId(Long userId) {
        try {
            if (!userRepository.existsById(userId)) {
                throw new EntityNotFoundException("User not found");
            }
            return commentRepository.findAllByAuthor_UserIdOrderByCreatedAtDesc(userId)
                    .stream()
                    .map(CommentDto.Response::from)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error getting user comments by user ID: {}", e.getMessage(), e);
            throw e;
        }
    }

    private void validateCommentAuthor(CommentEntity comment, Long userId) {
        if (!comment.getAuthor().getUserId().equals(userId)) {
            throw new IllegalArgumentException("Not authorized");
        }
    }
}