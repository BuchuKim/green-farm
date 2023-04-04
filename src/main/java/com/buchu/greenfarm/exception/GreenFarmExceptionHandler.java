package com.buchu.greenfarm.exception;

import com.buchu.greenfarm.dto.GreenFarmErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
@Slf4j
public class GreenFarmExceptionHandler {
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

    @ExceptionHandler({MethodArgumentTypeMismatchException.class,
            HttpRequestMethodNotSupportedException.class,
            HttpMediaTypeNotSupportedException.class,
            HttpMediaTypeNotAcceptableException.class,
            MissingPathVariableException.class,
            MissingServletRequestParameterException.class,
            MissingServletRequestPartException.class,
            ServletRequestBindingException.class,
            MethodArgumentNotValidException.class,
            NoHandlerFoundException.class,
            AsyncRequestTimeoutException.class,
            ErrorResponseException.class,
            ConversionNotSupportedException.class,
            TypeMismatchException.class,
            HttpMessageNotReadableException.class,
            HttpMessageNotWritableException.class,
            BindException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleMvcException(
            Exception e,
            HttpServletRequest request,
            Model model) {
        log.warn("{}! URL: {}",
                e.getClass().getName(),
                request.getRequestURI());
        model.addAttribute("error",
                GreenFarmErrorResponse.builder()
                        .errorName(e.getClass().getName())
                        .detailMessage(e.getMessage())
                        .build());
        return "error/error.html";
    }

    @ExceptionHandler(value = {Exception.class})
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
