package com.buchu.greenfarm.controller;

import com.buchu.greenfarm.config.auth.dto.SessionUser;
import com.buchu.greenfarm.dto.farmLog.CreateFarmLog;
import com.buchu.greenfarm.exception.GreenFarmErrorCode;
import com.buchu.greenfarm.exception.GreenFarmException;
import com.buchu.greenfarm.service.FarmLogService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {
    private final FarmLogService farmLogService;
    private final HttpSession httpSession;

    @GetMapping("/")
    public String getHome(Model model,
                             @RequestParam(value = "following",
                                     required = false,
                                     defaultValue = "false")
                             Boolean following,
                             Authentication authentication) {
        model.addAttribute("createFarmLog", new CreateFarmLog.Request());
        model.addAttribute("following", following);

        model.addAttribute("farmLogs",
                (following) ? farmLogService.getFollowingFarmLogs()
                        : farmLogService.getAllFarmLogs());
        return "index.html";
    }

    @GetMapping("/i")
    private String getInfoPage() {
        return "info.html";
    }

    private SessionUser getSessionUser() {
        SessionUser sessionUser = (SessionUser) httpSession.getAttribute("user");
        if (sessionUser != null) {
            return sessionUser;
        } else {
            throw new GreenFarmException(GreenFarmErrorCode.NO_AVAILABLE_FOLLOWING);
        }
    }

}
