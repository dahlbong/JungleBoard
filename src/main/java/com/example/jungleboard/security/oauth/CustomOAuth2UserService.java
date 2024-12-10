package com.example.jungleboard.security.oauth;

import com.example.jungleboard.domain.AuthProvider;
import com.example.jungleboard.domain.entity.UserEntity;
import com.example.jungleboard.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = super.loadUser(userRequest);

        String provider = userRequest.getClientRegistration().getRegistrationId();
        String providerId = oauth2User.getAttribute("sub");  // Google의 경우 'sub'가 유니크 ID
        String email = oauth2User.getAttribute("email");
        String name = oauth2User.getAttribute("name");

        UserEntity user = userRepository.findByEmail(email)
                .map(existingUser -> {
                    // 이미 가입된 경우, provider 정보 업데이트
                    if (existingUser.getProvider() == null) {
                        existingUser.setProvider(AuthProvider.GOOGLE);
                        existingUser.setProviderId(providerId);
                        return userRepository.save(existingUser);
                    }
                    return existingUser;
                })
                .orElseGet(() -> {
                    // 새로운 사용자 생성
                    UserEntity newUser = UserEntity.createOAuthUser(
                            email,
                            name,
                            AuthProvider.GOOGLE,
                            providerId
                    );
                    return userRepository.save(newUser);
                });

        return new CustomOAuth2User(oauth2User, user.getEmail());
    }
}