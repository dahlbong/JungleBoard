package com.example.jungleboard.dto;

import com.example.jungleboard.domain.entity.UserEntity;
import lombok.Getter;
import lombok.Setter;

public class UserDto {
    @Getter
    @Setter
    public static class SignUpRequest {
        private String email;
        private String password;
        private String nickname;
    }

    @Getter
    @Setter
    public static class LoginRequest {
        private String email;
        private String password;
    }

    @Getter
    @Setter
    public static class UpdateRequest {
        private String nickname;
        private String password;
    }

    @Getter
    public static class Response {
        private Long userId;
        private String email;
        private String nickname;

        private Response(Long userId, String email, String nickname) {
            this.userId = userId;
            this.email = email;
            this.nickname = nickname;
        }

        public static Response from(UserEntity user) {
            return new Response(
                    user.getUserId(),
                    user.getEmail(),
                    user.getNickname()
            );
        }
    }
}