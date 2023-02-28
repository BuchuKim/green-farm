package com.buchu.greenfarm.controller;

import com.buchu.greenfarm.config.auth.dto.SessionUser;
import com.buchu.greenfarm.dto.user.RegisterUserDto;
import com.buchu.greenfarm.service.LogInService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("u")
public class LoginController {
    private final HttpSession httpSession;
    private final LogInService logInService;

    @GetMapping("/login")
    public String getLogInPage() {
        // render OAuth2 login page
        return "login.html";
    }

    @GetMapping("/register")
    public String getRegisterPage(Model model) {
        // set-up ID & name at first registration
        model.addAttribute("registerUser",
                RegisterUserDto.fromSession(
                        (SessionUser) httpSession.getAttribute("user")));
        return "register.html";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("registerUser") final RegisterUserDto registerUserDto,
                           // @ModelAttribute : model 객체에 registerUser 자동으로 넣어주는 기능!
                           BindingResult bindingResult,
                           Model model) {
        if (bindingResult.hasErrors()) {
            return "register.html";
        }
        model.addAttribute("user",
                logInService.register(registerUserDto));
        return "redirect:/"+registerUserDto.getUserId();
    }

}
