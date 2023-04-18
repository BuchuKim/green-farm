package com.buchu.greenfarm.service;

import com.buchu.greenfarm.code.NotificationCode;
import com.buchu.greenfarm.dto.NotificationDto;
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
    public void sendTagAlarm(final List<String> taggedIds, final User taggingUser) {
        for (String id : taggedIds) {
            userRepository.findByUserId(id).ifPresent(
                    user -> {
                        notificationRepository.save(
                            Notification.builder()
                                    .notificationCode(NotificationCode.TAG_ALARM)
                                    .sendingUser(taggingUser)
                                    .receivingUser(user)
                                    .isRead(false)
                                    .build());
                    }
            );
        }
    }

    @Transactional
    public List<NotificationDto> getNotificationDtos(final String userId) {

        return notificationRepository.findByReceivingUser(
                userRepository.findByUserId(userId)
                        .orElseThrow(() -> new GreenFarmException(GreenFarmErrorCode.NO_USER_ERROR))
        ).orElse(new ArrayList<>())
                .stream().map(NotificationDto::fromEntity).toList();
    }


}
