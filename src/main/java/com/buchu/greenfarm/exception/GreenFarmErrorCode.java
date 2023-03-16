package com.buchu.greenfarm.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GreenFarmErrorCode {
    NO_FARM_LOG_ERROR("해당하는 농장 일기가 존재하지 않습니다."),
    NO_USER_ERROR("해당하는 유저가 존재하지 않습니다."),
    INVALID_REQUEST("잘못된 요청입니다."),
    DUPLICATED_USER_ID("중복된 유저 아이디입니다."),
    INTERNAL_SERVER_ERROR("서버에 오류가 발생했습니다."),
    INVALID_USER_ID_ERROR("적절한 아이디가 아닙니다."),
    INVALID_DATA("적절한 데이터가 아닙니다."),
    CANNOT_FOLLOW("팔로우 할 수 없는 유저입니다!"),
    CANNOT_EDIT("수정할 수 없습니다."),
    INVALID_OAUTH("잘못된 로그인 시도입니다."),
    NO_AVAILABLE_FOLLOWING("로그인 뒤 팔로우할 농장주를 찾아보세요."),
    INVALID_REQUEST_USER("요청에 적절한 사용자가 아닙니다."),
    ACCESS_DENIED("허용된 경로가 아닙니다."),
    NEED_LOGIN("로그인을 해주세요.");
    private final String detailMessage;
}
