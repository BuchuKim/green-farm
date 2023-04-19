package com.buchu.greenfarm.dto;

import com.buchu.greenfarm.code.NotificationCode;
import com.buchu.greenfarm.entity.Notification;
import com.buchu.greenfarm.exception.GreenFarmErrorCode;
import com.buchu.greenfarm.exception.GreenFarmException;
import com.buchu.greenfarm.util.TimeDuration;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class NotificationDto {
    private Long id;
    private String message;
    private NotificationCode notificationCode;
    private String notificationURL;
    private String timePassed;

    public static NotificationDto fromEntity(Notification notification) {
        return NotificationDto.builder()
                .id(notification.getNotificationId())
                .timePassed(TimeDuration.generateTimeDuration(notification.getCreatedAt()))
                .message(notification.getMessage())
                .notificationCode(notification.getNotificationCode())
                .notificationURL(findNotificationURL(notification))
                .build();
    }

    private static String findNotificationURL(Notification notification) {
        switch (notification.getNotificationCode()) {
            case TAG_ALARM -> {
                return "farm-log/" + notification.getFarmLog().getFarmLogId();
            }
            case FOLLOW_ALARM, LIKE_ALARM -> {
                return notification.getSendingUser().getUserId();
            }
            default -> {
                throw new GreenFarmException(GreenFarmErrorCode.UNKNOWN_ALARM_ERROR);
            }
        }
    }
}
