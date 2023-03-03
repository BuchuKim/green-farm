package com.buchu.greenfarm.config.auth;

import com.buchu.greenfarm.config.auth.dto.OAuthAttributes;
import com.buchu.greenfarm.config.auth.dto.SessionUser;
import com.buchu.greenfarm.entity.Role;
import com.buchu.greenfarm.entity.User;
import com.buchu.greenfarm.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;
    private final HttpSession httpSession;


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // 1. user request 로부터 user load
        OAuth2UserService delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        // 2. 구분
        // registrationId : 현재 어떤 OAuth service 이용중인가? (네이버? 구글?)
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        // userNameAttributeName : 로그인 시 'key' 역할 하는 "field 값"
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        OAuthAttributes attributes = OAuthAttributes.of(registrationId,
                userNameAttributeName,
                oAuth2User.getAttributes());

        User user = saveOrUpdate(attributes);
        httpSession.setAttribute("user", new SessionUser(user));

        return new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority(user.getRoleKey())),
                attributes.mapAttributes(),
                attributes.getNameAttributeKey());
    }

    @Transactional
    private User saveOrUpdate(OAuthAttributes attributes) {
        User user = userRepository.findByEmail(attributes.getEmail())
                .orElse(attributes.toEntity());

        return userRepository.save(user);
    }

    public void updateRole(String role) {
        DefaultOAuth2User currentUser = (DefaultOAuth2User)
                SecurityContextHolder.getContext()
                        .getAuthentication().getPrincipal();
        SecurityContextHolder.getContext().setAuthentication(
                new OAuth2AuthenticationToken(currentUser,
                        Collections.singleton(new SimpleGrantedAuthority(Role.USER.getKey())),
                        currentUser.getAttribute("registrationId")));
    }
}
