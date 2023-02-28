package com.buchu.greenfarm.controller;

import com.buchu.greenfarm.exception.GreenFarmErrorCode;
import com.buchu.greenfarm.exception.GreenFarmException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("e")
@RequiredArgsConstructor
@Slf4j
public class ErrorController {
    @GetMapping("/access-denied")
    public void throwAccessDeniedException() {
        throw new GreenFarmException(GreenFarmErrorCode.ACCESS_DENIED);
    }
}
