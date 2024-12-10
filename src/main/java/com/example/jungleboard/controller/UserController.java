package com.example.jungleboard.controller;

import com.example.jungleboard.dto.CommentDto;
import com.example.jungleboard.dto.PostDto;
import com.example.jungleboard.dto.UserDto;
import com.example.jungleboard.service.CommentService;
import com.example.jungleboard.service.PostService;
import com.example.jungleboard.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final PostService postService;
    private final CommentService commentService;

    // User endpoints
    @PostMapping("/signup")
    public ResponseEntity<UserDto.Response> signup(@RequestBody UserDto.SignUpRequest request) {
        return ResponseEntity.ok(userService.signup(request));
    }

    @PostMapping("/login")
    public ResponseEntity<UserDto.Response> login(@RequestBody UserDto.LoginRequest request) {
        return ResponseEntity.ok(userService.login(request));
    }

    @PutMapping("/users/{userId}")
    public ResponseEntity<UserDto.Response> updateUser(
            @PathVariable Long userId,
            @RequestBody UserDto.UpdateRequest request) {
        return ResponseEntity.ok(userService.updateUser(userId, request));
    }

    // My Page endpoints
    @GetMapping("/users/{userId}/posts")
    public ResponseEntity<List<PostDto.Response>> getUserPosts(@PathVariable Long userId) {
        return ResponseEntity.ok(postService.getUserPostsByUserId(userId));
    }

    @GetMapping("/users/{userId}/comments")
    public ResponseEntity<List<CommentDto.Response>> getUserComments(@PathVariable Long userId) {
        return ResponseEntity.ok(commentService.getUserComments(userId));
    }
}
