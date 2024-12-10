package com.example.jungleboard.domain.entity;

import com.example.jungleboard.domain.AuthProvider;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(unique = true)
    private String email;

    @Column(nullable = true)
    private String password;
    private String nickname;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private List<PostEntity> posts = new ArrayList<>();

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private List<CommentEntity> comments = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AuthProvider provider = AuthProvider.LOCAL;    // GOOGLE, LOCAL

    private String providerId;        // 소셜 로그인의 경우 제공되는 ID

    public static UserEntity createUser(String email, String password, String nickname) {
        UserEntity user = new UserEntity();
        user.email = email;
        user.password = password;
        user.nickname = nickname;
        user.provider = AuthProvider.LOCAL;
        return user;
    }

    public static UserEntity createOAuthUser(String email, String nickname, AuthProvider provider, String providerId) {
        UserEntity user = new UserEntity();
        user.email = email;
        user.nickname = nickname;
        user.provider = provider;
        user.providerId = providerId;
        // password는 null로 둠
        return user;
    }
}