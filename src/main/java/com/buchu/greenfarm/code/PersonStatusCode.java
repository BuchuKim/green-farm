package com.buchu.greenfarm.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PersonStatusCode {
    DORMANT("휴면"),
    DELETED("삭제"),
    EXIST("존재");

    private final String description;
}
