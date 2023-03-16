package com.buchu.greenfarm.exception;

import com.buchu.greenfarm.dto.GreenFarmErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Slf4j
public class GreenFarmExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(GreenFarmException.class)
    public String handleGreenFarmException(
            final GreenFarmException e,
            final HttpServletRequest request,
            Model model) {
        log.warn("Green Farm Exception!! URL: {}",
                request.getRequestURI());
        model.addAttribute("error",
                GreenFarmErrorResponse.builder()
                        .errorName(e.getGreenFarmErrorCode().name())
                        .detailMessage(e.getDetailMessage())
                        .build());
        return "error/error.html";
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleArgsException(
            MethodArgumentTypeMismatchException e,
            HttpServletRequest request,
            Model model) {
        log.warn("{}! URL: {}",
                e.getErrorCode(),
                request.getRequestURI());
        model.addAttribute("error",
                GreenFarmErrorResponse.builder()
                        .errorName(e.getErrorCode())
                        .detailMessage(e.getMessage())
                        .build());
        return "error/error.html";
    }

    @ExceptionHandler(value = {Exception.class, RuntimeException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleRemainException(
            Exception e,
            HttpServletRequest request,
            Model model) {

        log.warn("!! Unknown error! URL: {}\nStack Trace: {}",
                request.getRequestURI(),
                e.getStackTrace());
        log.warn("e.getMessage(): {}",e.getMessage());
        model.addAttribute("error",
                GreenFarmErrorResponse.builder()
                        .errorName("UNKNOWN ERROR!")
                        .detailMessage(e.getMessage())
                        .build());
        return "error/error.html";
    }
}
