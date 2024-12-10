package com.example.jungleboard.dto;

import com.example.jungleboard.domain.entity.CommentEntity;
import com.example.jungleboard.domain.entity.PostEntity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class PostDto {
    @Getter
    @Setter
    public static class CreateRequest {
        private String title;
        private String content;
    }

    @Getter
    @Setter
    public static class UpdateRequest {
        private String title;
        private String content;
    }

    @Getter
    public static class Response {
        private Long postId;
        private String title;
        private String content;
        private String authorNickname;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private List<CommentResponse> comments;

        private Response(Long postId, String title, String content, String authorNickname,
                         LocalDateTime createdAt, LocalDateTime updatedAt, List<CommentResponse> comments) {
            this.postId = postId;
            this.title = title;
            this.content = content;
            this.authorNickname = authorNickname;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
            this.comments = comments;
        }

        public static Response from(PostEntity post) {
            List<CommentResponse> commentResponses = post.getComments().stream()
                    .map(CommentResponse::from)
                    .collect(Collectors.toList());

            // author가 null일 경우의 처리 추가
            String authorNickname = post.getAuthor() != null ?
                    post.getAuthor().getNickname() :
                    "탈퇴한 사용자";  // 또는 "Unknown" 등 원하는 텍스트

            return new Response(
                    post.getPostId(),
                    post.getTitle(),
                    post.getPostContent(),
                    authorNickname,
                    post.getCreatedAt(),
                    post.getUpdatedAt(),
                    commentResponses
            );
        }

        @Getter
        public static class CommentResponse {
            private Long commentId;
            private String content;
            private String authorNickname;
            private LocalDateTime createdAt;

            private CommentResponse(Long commentId, String content, String authorNickname, LocalDateTime createdAt) {
                this.commentId = commentId;
                this.content = content;
                this.authorNickname = authorNickname;
                this.createdAt = createdAt;
            }

            public static CommentResponse from(CommentEntity comment) {
                return new CommentResponse(
                        comment.getCommentId(),
                        comment.getCommentContent(),
                        comment.getAuthor().getNickname(),
                        comment.getCreatedAt()
                );
            }
        }
    }
}