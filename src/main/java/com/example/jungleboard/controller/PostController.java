package com.example.jungleboard.controller;

import com.example.jungleboard.dto.PostDto;
import com.example.jungleboard.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostController {
    private final PostService postService;

    @PostMapping("/posts")
    public ResponseEntity<PostDto.Response> createPost(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody PostDto.CreateRequest request) {
        return ResponseEntity.ok(postService.createPost(userDetails.getUsername(), request));
    }

    @GetMapping("/posts/{postId}")
    public ResponseEntity<PostDto.Response> getPost(@PathVariable Long postId) {
        return ResponseEntity.ok(postService.getPost(postId));
    }

    @GetMapping("/posts")
    public ResponseEntity<List<PostDto.Response>> getAllPosts() {
        return ResponseEntity.ok(postService.getAllPosts());
    }

    @PutMapping("/posts/{postId}")
    public ResponseEntity<PostDto.Response> updatePost(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long postId,
            @RequestBody PostDto.UpdateRequest request) {
        return ResponseEntity.ok(postService.updatePost(userDetails.getUsername(), postId, request));
    }

    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<Void> deletePost(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long postId) {
        postService.deletePost(userDetails.getUsername(), postId);
        return ResponseEntity.ok().build();
    }
}