package com.buchu.greenfarm.service;

import com.buchu.greenfarm.config.auth.CustomOAuth2UserService;
import com.buchu.greenfarm.config.auth.dto.SessionUser;
import com.buchu.greenfarm.dto.user.RegisterUserDto;
import com.buchu.greenfarm.dto.user.UserProfileDto;
import com.buchu.greenfarm.entity.Role;
import com.buchu.greenfarm.entity.User;
import com.buchu.greenfarm.exception.GreenFarmErrorCode;
import com.buchu.greenfarm.exception.GreenFarmException;
import com.buchu.greenfarm.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class LogInService {
    private final UserRepository userRepository;
    private final HttpSession httpSession;
    private final CustomOAuth2UserService customOAuth2UserService;

    @Transactional
    public UserProfileDto register(@NonNull RegisterUserDto registerUserDto) {
        validateRegisterRequest(registerUserDto);
        User foundUser = userRepository.findByEmail(registerUserDto.getEmail())
                .orElseThrow(()-> new GreenFarmException(GreenFarmErrorCode.NO_USER_ERROR));
        foundUser.setName(registerUserDto.getName());
        foundUser.setUserId(registerUserDto.getUserId());
        foundUser.setBio(registerUserDto.getBio());
        foundUser.setRole(Role.USER);

        httpSession.setAttribute("user", new SessionUser(foundUser));
        customOAuth2UserService.updateRole(Role.USER.getKey());
        return UserProfileDto.fromEntity(foundUser);
    }

    @Transactional
    private void validateRegisterRequest(@NonNull RegisterUserDto registerUserDto) {
        // no duplicated user ID
        userRepository.findByUserId(registerUserDto.getUserId())
                .ifPresent((user) -> {
                    throw new GreenFarmException(GreenFarmErrorCode.DUPLICATED_USER_ID);
                });
    }
}
