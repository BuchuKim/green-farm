package com.buchu.greenfarm.exception;

import com.buchu.greenfarm.dto.GreenFarmErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GreenFarmExceptionHandler {
    @ExceptionHandler(GreenFarmException.class)
    public String handleGreenFarmException(
            final GreenFarmException e,
            final HttpServletRequest request,
            Model model) {
        log.info("Green Farm Exception!! URL: {}, Stack Trace: {}",
                request.getRequestURI(),
                e.getStackTrace());
        model.addAttribute("error",
                GreenFarmErrorResponse.builder()
                        .errorName(e.getGreenFarmErrorCode().name().toString())
                        .detailMessage(e.getDetailMessage())
                        .build());
        return "error.html";
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public String handleValidationException(
            final ConstraintViolationException e,
            final HttpServletRequest request,
            Model model) {
        log.info("Green Farm Exception!! URL: {}, Stack Trace: {}",
                request.getRequestURI(),
                e.getStackTrace());

        model.addAttribute("error",
                GreenFarmErrorResponse.builder()
                        .errorName(e.getLocalizedMessage())
                        .detailMessage(e.getMessage())
                        .build());

        return "error.html";
    }

    @ExceptionHandler(Exception.class)
    public String handleRemainException(
            Exception e,
            HttpServletRequest request,
            Model model) {

        log.info("!! Unknown error! URL: {}, Stack Trace: {}",
                request.getRequestURI(),
                e.getStackTrace());
        model.addAttribute("error",
                GreenFarmErrorResponse.builder()
                        .errorName(e.getLocalizedMessage())
                        .detailMessage(e.getMessage())
                        .build());
        return "error.html";
    }
}
