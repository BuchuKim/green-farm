package com.buchu.greenfarm.controller;

import com.buchu.greenfarm.dto.farmLog.CreateFarmLog;
import com.buchu.greenfarm.exception.GreenFarmErrorCode;
import com.buchu.greenfarm.exception.GreenFarmException;
import com.buchu.greenfarm.service.FarmLogService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("farm-log")
@RequiredArgsConstructor
@Slf4j
public class FarmLogController {
    private final FarmLogService farmLogService;
    private final HttpSession httpSession;

    @GetMapping("/{id}")
    public String showFarmLog(
            HttpServletRequest request,
            @PathVariable Long id,
            Model model) {
        if (request.getHeader("referer") == null) {
            model.addAttribute("backURI","/");
        } else if (hasSameURI(request.getRequestURI(),
                request.getHeader("referer"))) {
            model.addAttribute("backURI", httpSession.getAttribute("backURI"));
        } else {
            httpSession.setAttribute("backURI",
                    request.getHeader("referer"));
            model.addAttribute("backURI",
                    request.getHeader("referer"));
        }
        model.addAttribute("farmLog",
                farmLogService.getFarmLogDetail(id));
        return "showFarmLog.html";
    }

    @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String createFarmLog(
            @Valid @ModelAttribute("createFarmLog")
            final CreateFarmLog.Request request,
            BindingResult result
            ) {
        if (result.hasErrors()) {
            throw new GreenFarmException(GreenFarmErrorCode.INVALID_DATA);
        }
        return "redirect:/farm-log/" + String.valueOf(
                farmLogService.getCreatedFarmLogId(request));
    }

    @DeleteMapping(value = "/{id}")
    public String deleteFarmLog(
            @PathVariable("id") final Long farmLogId
    ) {
        log.info("deleting farm log of id: {}", farmLogId);
        farmLogService.deleteFarmLog(farmLogId);
        return "redirect:/";
    }

    @PostMapping("/like/{farmLogId}")
    public String likeFarmLog(@PathVariable("farmLogId")
                                  final Long farmLogId) {
        farmLogService.likeFarmLog(farmLogId);
        return "redirect:/farm-log/"+farmLogId;
    }

    @DeleteMapping("/like/{farmLogId}")
    public String unlikeFarmLog(@PathVariable("farmLogId")
                                    final Long farmLogId) {
        farmLogService.unlikeFarmLog(farmLogId);
        return "redirect:/farm-log/"+farmLogId;
    }

    private Boolean hasSameURI(@NonNull String request,
                               String referer) {
        if (referer==null) {
            return false;
        }
        return referer.endsWith(request);
    }
}
