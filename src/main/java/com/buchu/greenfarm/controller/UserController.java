package com.buchu.greenfarm.controller;

import com.buchu.greenfarm.config.auth.dto.SessionUser;
import com.buchu.greenfarm.dto.user.UserDto;
import com.buchu.greenfarm.dto.user.UserProfileDto;
import com.buchu.greenfarm.entity.User;
import com.buchu.greenfarm.exception.GreenFarmErrorCode;
import com.buchu.greenfarm.exception.GreenFarmException;
import com.buchu.greenfarm.repository.FarmLogRepository;
import com.buchu.greenfarm.service.NotificationService;
import com.buchu.greenfarm.service.UserService;
import com.buchu.greenfarm.util.PageRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;
    private final NotificationService notificationService;
    private final HttpSession httpSession;

    @GetMapping("/d-warn")
    private String getWarningPage(Model model) {
        model.addAttribute("user",
                userService.getBeingDeletedUser());
        return "warning.html";
    }

    @GetMapping("/{userId}")
    public String getUser(@PathVariable("userId") final String userId,
                          @RequestParam(value = "like",
                                  required = false,
                                  defaultValue = "false")
                                    final Boolean isLike,
                          final PageRequest pageRequest,
                          Model model) {
        User currentPageUser = userService.getUserByUserId(userId);
        UserDto currentPageUserDto = userService.getUserDetail(currentPageUser);
        Map<String, Object> data = userService.getFarmLogPagesOfCurrentPageUser(
                currentPageUser, isLike, pageRequest.of());

        model.addAttribute("pageNum",pageRequest.getPage());
        model.addAttribute("hasNext",data.get("hasNext"));
        model.addAttribute("currentPageUser", currentPageUserDto);
        model.addAttribute("isLike",isLike);
        model.addAttribute("farmLogs", data.get("farmLogs"));

        return "profile.html";
    }

    @PutMapping("/{userId}")
    public String editUser(@PathVariable("userId") final String userId,
                           @Valid @ModelAttribute("user") UserProfileDto user,
                           BindingResult result) {
        if (result.hasErrors()) {
            return "editUser.html";
        }
        userService.editUser(userId,user);
        return "redirect:/"+user.getUserId();
    }

    @DeleteMapping("/{userId}")
    public String deleteUser(HttpServletRequest request,
                             HttpServletResponse response,
                             @PathVariable("userId") final String userId) {
        userService.deleteUser(userId);
        new SecurityContextLogoutHandler().logout(
                request,
                response,
                SecurityContextHolder.getContext().getAuthentication());
        return "redirect:/";
    }

    @PostMapping("/{userId}/follow")
    public String followUser(@PathVariable("userId") final String userId) {
        userService.follow(
                getSessionUser().getUserId(),
                userId);
        return "redirect:/"+userId;
    }

    @DeleteMapping("/{userId}/follow")
    public String unfollowUser(@PathVariable("userId") final String userId) {
        userService.unfollow(
                getSessionUser().getUserId(),
                userId);
        return "redirect:/"+userId;
    }

    @GetMapping("/{userId}/following")
    public String getFollowing(@PathVariable("userId") final String userId,
                               Model model) {
        model.addAttribute("userId", userId);
        model.addAttribute("followings",
                userService.getFollowingUserDto(userId));
        return "following.html";
    }

    @GetMapping("/{userId}/follower")
    public String getFollower(@PathVariable("userId") final String userId,
                               Model model) {
        model.addAttribute("userId",userId);
        model.addAttribute("followers",
                userService.getFollowerUserDto(userId));
        return "follower.html";
    }

    @GetMapping("/{userId}/settings")
    public String modifyProfile(@PathVariable("userId") final String userId,
                                Model model) {
        model.addAttribute("user",
                userService.getUserProfileDto(userId));
        return "editUser.html";
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
