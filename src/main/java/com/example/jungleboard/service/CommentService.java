package com.example.jungleboard.service;

import com.example.jungleboard.domain.entity.CommentEntity;
import com.example.jungleboard.domain.entity.PostEntity;
import com.example.jungleboard.domain.entity.UserEntity;
import com.example.jungleboard.domain.repository.CommentRepository;
import com.example.jungleboard.domain.repository.PostRepository;
import com.example.jungleboard.domain.repository.UserRepository;
import com.example.jungleboard.dto.CommentDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public CommentDto.Response createComment(Long userId, Long postId, CommentDto.CreateRequest request) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        PostEntity post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        CommentEntity comment = CommentEntity.createComment(
                user,
                post,
                request.getContent()
        );

        CommentEntity savedComment = commentRepository.save(comment);
        return CommentDto.Response.from(savedComment);
    }

    public List<CommentDto.Response> getUserComments(Long userId) {
        validateUser(userId);
        return commentRepository.findAllByPost_PostIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(CommentDto.Response::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public CommentDto.Response updateComment(Long userId, Long commentId, CommentDto.UpdateRequest request) {
        CommentEntity comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));

        validateCommentAuthor(comment, userId);

        comment.updateContent(request.getContent());
        return CommentDto.Response.from(comment);
    }

    @Transactional
    public void deleteComment(Long userId, Long commentId) {
        CommentEntity comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));

        validateCommentAuthor(comment, userId);

        commentRepository.delete(comment);
    }

    private void validateUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("User not found");
        }
    }

    private void validateCommentAuthor(CommentEntity comment, Long userId) {
        if (!comment.getAuthor().getUserId().equals(userId)) {
            throw new IllegalArgumentException("Not authorized");
        }
    }

    public List<CommentDto.Response> getPostComments(Long postId) {
        return commentRepository.findAllByPost_PostIdOrderByCreatedAtDesc(postId)
                .stream()
                .map(CommentDto.Response::from)
                .collect(Collectors.toList());
    }
}