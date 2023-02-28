package com.buchu.greenfarm.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FarmLogStatusCode {
    DELETED("삭제됨"),
    PRESENTED("존재함"),
    PROTECTED("보호됨");
    private String description;
}
