package com.buchu.greenfarm.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NotificationCode {
    TAG_ALARM("태그 알림"),
    LIKE_ALARM("좋아요 알림"),
    FOLLOW_ALARM("팔로우 알림");
    private final String detail;
}
