package com.buchu.greenfarm.config.auth;

import com.buchu.greenfarm.config.auth.dto.SessionUser;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final HttpSession httpSession;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {
        SessionUser currentUser =
                (SessionUser) httpSession.getAttribute("user");
        if (currentUser.getUserId().equals("_")) {
            response.sendRedirect("/u/register");
        } else {
            response.sendRedirect("/"+currentUser.getUserId());
        }
    }
}
