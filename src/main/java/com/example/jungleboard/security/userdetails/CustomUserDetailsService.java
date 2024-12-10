package com.example.jungleboard.security.userdetails;

import com.example.jungleboard.domain.entity.UserEntity;
import com.example.jungleboard.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        return createUserDetails(user);
    }

    private UserDetails createUserDetails(UserEntity user) {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_USER");
        return User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .authorities(Collections.singleton(authority))
                .build();
    }
}