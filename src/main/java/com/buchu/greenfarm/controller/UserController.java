package com.buchu.greenfarm.controller;

import com.buchu.greenfarm.config.auth.dto.SessionUser;
import com.buchu.greenfarm.dto.user.UserProfileDto;
import com.buchu.greenfarm.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;
    private final HttpSession httpSession;

    @GetMapping("/d-warn")
    private String getWarningPage(Model model) {
        model.addAttribute("user",
                userService.getBeingDeletedUser());
        return "warning.html";
    }

    @GetMapping("/{userId}")
    public String getUser(@PathVariable("userId") final String userId,
                          @RequestParam(value = "like", required = false, defaultValue = "false")
                                  final Boolean isLike,
                          Authentication authentication,
                          Model model) {
        model.addAttribute("userDetail",
                userService.getUserDetailDto(userId, isLike));
        model.addAttribute("isFollowing",
                authentication != null
                        && authentication.isAuthenticated()
                        && userService.isFollowing(
                                getSessionUser().getUserId(), userId));
        model.addAttribute("isLike",isLike);
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
    public String deleteUser(@PathVariable("userId") final String userId) {
        log.info("user id - {} deleted.",userId);
        userService.deleteUser(userId);
        return "redirect:/u/logout";
    }

    @GetMapping("/{userId}/follow")
    public String followUser(@PathVariable("userId") final String userId,
                          Model model) {
        log.info(getSessionUser().getUserId() +
                " is trying to follow " +
                userId);
        userService.follow(
                getSessionUser().getUserId(),
                userId);
        return "redirect:/"+userId;
    }

    @GetMapping("/{userId}/unfollow")
    public String unfollowUser(@PathVariable("userId") final String userId,
                             Model model) {
        log.info(getSessionUser().getUserId() +
                " is trying to unfollow " +
                userId);
        userService.unfollow(
                getSessionUser().getUserId(),
                userId);
        return "redirect:/"+userId;
    }

    @GetMapping("/{userId}/following")
    public String getFollowing(@PathVariable("userId") final String userId,
                               Model model) {
        model.addAttribute("user",
                userService.getUserProfileDto(userId));
        model.addAttribute("followings",
                userService.getFollowingUserDto(userId));
        return "following.html";
    }

    @GetMapping("/{userId}/follower")
    public String getFollower(@PathVariable("userId") final String userId,
                               Model model) {
        model.addAttribute("user",
                userService.getUserProfileDto(userId));
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
        return (SessionUser) httpSession.getAttribute("user");
    }
}
