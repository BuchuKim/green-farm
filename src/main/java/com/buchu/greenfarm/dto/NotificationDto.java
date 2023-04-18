package com.buchu.greenfarm.dto;

import com.buchu.greenfarm.code.NotificationCode;
import com.buchu.greenfarm.entity.Notification;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class NotificationDto {
    private LocalDateTime createdAt;
    private String message;
    private NotificationCode notificationCode;

    public static NotificationDto fromEntity(Notification notification) {
        return NotificationDto.builder()
                .createdAt(notification.getCreatedAt())
                .message(notification.getMessage())
                .notificationCode(notification.getNotificationCode())
                .build();
    }
}
