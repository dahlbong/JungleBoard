package com.example.jungleboard.dto;

import com.example.jungleboard.domain.entity.CommentEntity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

public class CommentDto {
    @Getter
    @Setter
    public static class CreateRequest {
        private String content;
    }

    @Getter
    @Setter
    public static class UpdateRequest {
        private String content;
    }

    @Getter
    public static class Response {
        private Long commentId;
        private String content;
        private String authorNickname;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        private Response(Long commentId, String content, String authorNickname,
                         LocalDateTime createdAt, LocalDateTime updatedAt) {
            this.commentId = commentId;
            this.content = content;
            this.authorNickname = authorNickname;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
        }

        public static Response from(CommentEntity comment) {
            return new Response(
                    comment.getCommentId(),
                    comment.getCommentContent(),
                    comment.getAuthor().getNickname(),
                    comment.getCreatedAt(),
                    comment.getUpdatedAt()
            );
        }
    }
}