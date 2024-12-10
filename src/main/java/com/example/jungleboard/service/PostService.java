package com.example.jungleboard.service;

import com.example.jungleboard.domain.entity.PostEntity;
import com.example.jungleboard.domain.entity.UserEntity;
import com.example.jungleboard.domain.repository.CommentRepository;
import com.example.jungleboard.domain.repository.PostRepository;
import com.example.jungleboard.domain.repository.UserRepository;
import com.example.jungleboard.dto.PostDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public PostDto.Response createPost(String email, PostDto.CreateRequest request) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        PostEntity post = PostEntity.createPost(
                user,
                request.getTitle(),
                request.getContent()
        );

        PostEntity savedPost = postRepository.save(post);
        return PostDto.Response.from(savedPost);
    }

    public PostDto.Response getPost(Long postId) {
        PostEntity post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        return PostDto.Response.from(post);
    }

    public List<PostDto.Response> getAllPosts() {
        List<PostEntity> posts = postRepository.findAllByOrderByCreatedAtDesc();
        return posts.stream()
                .map(PostDto.Response::from)
                .collect(Collectors.toList());
    }

    public List<PostDto.Response> getUserPosts(String email) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        List<PostEntity> posts = postRepository.findAllByAuthor_UserIdOrderByCreatedAtDesc(user.getUserId());
        return posts.stream()
                .map(PostDto.Response::from)
                .collect(Collectors.toList());
    }

    public List<PostDto.Response> getUserPostsByUserId(Long userId) {
        // 사용자 존재 여부 확인
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("User not found");
        }

        List<PostEntity> posts = postRepository.findAllByAuthor_UserIdOrderByCreatedAtDesc(userId);
        return posts.stream()
                .map(PostDto.Response::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public PostDto.Response updatePost(String email, Long postId, PostDto.UpdateRequest request) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        PostEntity post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        if (!post.getAuthor().getUserId().equals(user.getUserId())) {
            throw new IllegalArgumentException("Not authorized");
        }

        post.update(request.getTitle(), request.getContent());
        return PostDto.Response.from(post);
    }

    @Transactional
    public void deletePost(String email, Long postId) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        PostEntity post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        if (!post.getAuthor().getUserId().equals(user.getUserId())) {
            throw new IllegalArgumentException("Not authorized");
        }

        commentRepository.deleteAllByPost_PostId(postId);
        postRepository.delete(post);
    }
}