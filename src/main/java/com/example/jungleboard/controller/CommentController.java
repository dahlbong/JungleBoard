package com.example.jungleboard.controller;

import com.example.jungleboard.domain.entity.UserEntity;
import com.example.jungleboard.domain.repository.UserRepository;
import com.example.jungleboard.dto.CommentDto;
import com.example.jungleboard.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentController {
    private final CommentService commentService;
    private final UserRepository userRepository;

    @PostMapping("/posts/{postId}")
    public ResponseEntity<CommentDto.Response> createComment(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long postId,
            @RequestBody CommentDto.CreateRequest request) {
        UserEntity user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return ResponseEntity.ok(commentService.createComment(user.getEmail(), postId, request));
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<CommentDto.Response> updateComment(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long commentId,
            @RequestBody CommentDto.UpdateRequest request) {
        UserEntity user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return ResponseEntity.ok(commentService.updateComment(user.getEmail(), commentId, request));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long commentId) {
        UserEntity user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        commentService.deleteComment(user.getEmail(), commentId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/posts/{postId}")
    public ResponseEntity<List<CommentDto.Response>> getPostComments(@PathVariable Long postId) {
        return ResponseEntity.ok(commentService.getPostComments(postId));
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<List<CommentDto.Response>> getUserComments(
            @PathVariable Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return ResponseEntity.ok(commentService.getUserCommentsByUserId(user.getUserId()));
    }
}