package com.buchu.greenfarm.config.auth.dto;

import com.buchu.greenfarm.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Builder
@ToString
@RequiredArgsConstructor
public class SessionUser implements Serializable {
    private final String name;
    private final String email;
    private final String userId;

    public SessionUser(User user) {
        this.name = user.getName();
        this.email = user.getEmail();
        this.userId = user.getUserId();
    }
}
