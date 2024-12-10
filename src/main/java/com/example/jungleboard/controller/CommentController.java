package com.example.jungleboard.controller;

import com.example.jungleboard.dto.CommentDto;
import com.example.jungleboard.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")  // base path 수정
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<CommentDto.Response> createComment(
            @RequestHeader("X-USER-ID") Long userId,
            @PathVariable Long postId,
            @RequestBody CommentDto.CreateRequest request) {
        return ResponseEntity.ok(commentService.createComment(userId, postId, request));
    }

    @PutMapping("/comments/{commentId}")
    public ResponseEntity<CommentDto.Response> updateComment(
            @RequestHeader("X-USER-ID") Long userId,
            @PathVariable Long commentId,
            @RequestBody CommentDto.UpdateRequest request) {
        return ResponseEntity.ok(commentService.updateComment(userId, commentId, request));
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @RequestHeader("X-USER-ID") Long userId,
            @PathVariable Long commentId) {
        commentService.deleteComment(userId, commentId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<List<CommentDto.Response>> getPostComments(@PathVariable Long postId) {
        return ResponseEntity.ok(commentService.getPostComments(postId));
    }
}