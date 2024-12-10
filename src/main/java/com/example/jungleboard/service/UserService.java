package com.example.jungleboard.service;

import com.example.jungleboard.domain.entity.UserEntity;
import com.example.jungleboard.domain.repository.UserRepository;
import com.example.jungleboard.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserDto.Response signup(UserDto.SignUpRequest request) {
        validateDuplicateEmail(request.getEmail());
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        UserEntity user = UserEntity.createUser(
                request.getEmail(),
                encodedPassword,
                request.getNickname()
        );

        UserEntity savedUser = userRepository.save(user);
        return UserDto.Response.from(savedUser);
    }

    @Transactional
    public UserDto.Response login(UserDto.LoginRequest request) {
        UserEntity user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (!user.getPassword().equals(request.getPassword())) {
            throw new IllegalArgumentException("Invalid password");
        }

        return UserDto.Response.from(user);
    }

    @Transactional
    public UserDto.Response updateUser(Long userId, UserDto.UpdateRequest request) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (request.getNickname() != null) {
            user.setNickname(request.getNickname());
        }
        if (request.getPassword() != null) {
            user.setPassword(request.getPassword());
        }

        return UserDto.Response.from(user);
    }

    private void validateDuplicateEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already exists");
        }
    }
}