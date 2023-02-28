package com.buchu.greenfarm.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    ADMIN("ROLE_ADMIN","관리자 유저"),
    GUEST("ROLE_GUEST","게스트 유저"),
    USER("ROLE_USER", "기본 유저");
    private final String key;
    private final String description;
}
