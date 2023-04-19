package com.buchu.greenfarm.controller;

import com.buchu.greenfarm.config.auth.dto.SessionUser;
import com.buchu.greenfarm.exception.GreenFarmErrorCode;
import com.buchu.greenfarm.exception.GreenFarmException;
import com.buchu.greenfarm.service.NotificationService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;
    private final HttpSession httpSession;

    @GetMapping("/notifications")
    public String getNotificationPage(Model model) {

        model.addAttribute("notifications",
                notificationService.getNotificationDtos(
                        getSessionUser().getUserId()));
        return "notifications.html";
    }

    @DeleteMapping("/notifications")
    public String deleteAllNotifications() {
        notificationService.deleteAllNotifications(getSessionUser().getUserId());
        return "notifications.html";
    }

    @DeleteMapping("/notification/{notificationId}")
    public String deleteNotification(@PathVariable("notificationId")
                                         final Long notificationId) {
        notificationService.deleteNotification(notificationId);
        return "redirect:/notifications";
    }

    private SessionUser getSessionUser() {
        SessionUser sessionUser = (SessionUser) httpSession.getAttribute("user");

        if (sessionUser == null) {
            throw new GreenFarmException(GreenFarmErrorCode.NEED_LOGIN);
        } else {
            return sessionUser;
        }
    }
}
