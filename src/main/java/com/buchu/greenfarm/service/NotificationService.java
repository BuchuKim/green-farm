package com.buchu.greenfarm.service;

import com.buchu.greenfarm.code.NotificationCode;
import com.buchu.greenfarm.dto.NotificationDto;
import com.buchu.greenfarm.entity.FarmLog;
import com.buchu.greenfarm.entity.Notification;
import com.buchu.greenfarm.entity.User;
import com.buchu.greenfarm.exception.GreenFarmErrorCode;
import com.buchu.greenfarm.exception.GreenFarmException;
import com.buchu.greenfarm.repository.NotificationRepository;
import com.buchu.greenfarm.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationService {
    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;
    private final HttpSession httpSession;

    public List<String> searchTagList(final String logContent) {
        Pattern idPattern = Pattern.compile("@[a-zA-Z][a-zA-Z0-9]{0,14}");
        return Arrays.stream(logContent.split(" "))
                .filter(s -> idPattern.matcher(s).matches())
                .map(s -> s.substring(1))
                .toList();
    }

    @Transactional
    public void sendTagNotification(final List<String> taggedIds,
                             final User taggingUser,
                             final FarmLog taggingFarmLog) {
        for (String id : taggedIds) {
            userRepository.findByUserId(id).ifPresent(
                    user -> {
                        notificationRepository.save(
                            Notification.builder()
                                    .notificationCode(NotificationCode.TAG_ALARM)
                                    .sendingUser(taggingUser)
                                    .receivingUser(user)
                                    .farmLog(taggingFarmLog)
                                    .isRead(false)
                                    .build());
                    }
            );
        }
    }

    @Transactional
    public void sendFollowNotification(final User followingUser,
                                       final User followedUser) {
        notificationRepository.save(
                Notification.builder()
                        .sendingUser(followingUser)
                        .receivingUser(followedUser)
                        .notificationCode(NotificationCode.FOLLOW_ALARM)
                        .isRead(false)
                        .build()
        );
    }

    @Transactional
    public void sendLikeNotification(final User sendingUser,
                                     final FarmLog farmLog) {
        notificationRepository.save(
                Notification.builder()
                        .sendingUser(sendingUser)
                        .receivingUser(farmLog.getAuthor())
                        .farmLog(farmLog)
                        .notificationCode(NotificationCode.LIKE_ALARM)
                        .build());
    }

    @Transactional
    public List<NotificationDto> getNotificationDtos(final String userId) {
        User currentUser = userRepository.findByUserId(userId)
                .orElseThrow(() -> new GreenFarmException(GreenFarmErrorCode.NO_USER_ERROR));

        List<Notification> notifications =
                notificationRepository
                        .findByReceivingUser(currentUser)
                .orElse(new ArrayList<>());

        notificationRepository.deleteExceptFor(
                currentUser,
                notifications.stream().map(Notification::getNotificationId).toList());
        return notifications
                .stream().map(NotificationDto::fromEntity).toList();
    }

    @Transactional
    public void deleteNotification(final Long notificationId) {
        notificationRepository.deleteById(notificationId);
    }

    @Transactional
    public void deleteAllNotifications(final String userId) {
        notificationRepository.deleteByReceivingUser(
                userRepository.findByUserId(userId).orElseThrow(
                        () -> new GreenFarmException(GreenFarmErrorCode.NO_USER_ERROR)));
    }

}
