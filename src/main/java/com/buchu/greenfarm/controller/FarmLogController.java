package com.buchu.greenfarm.controller;

import com.buchu.greenfarm.dto.farmLog.CreateFarmLog;
import com.buchu.greenfarm.dto.farmLog.FarmLogDto;
import com.buchu.greenfarm.entity.FarmLog;
import com.buchu.greenfarm.exception.GreenFarmErrorCode;
import com.buchu.greenfarm.exception.GreenFarmException;
import com.buchu.greenfarm.service.FarmLogService;
import com.buchu.greenfarm.util.PageRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
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
        model.addAttribute("backURI", getBackURI(request));
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

    @GetMapping("/search")
    public String searchFarLog(Model model,
                                @RequestParam(value = "keyword",
                                        defaultValue = "")
                                final String keyword,
                               PageRequest pageRequest) {

        Page<FarmLog> farmLogPage = farmLogService.searchFarmLog(keyword, pageRequest.of());

        model.addAttribute("keyword", keyword);
        model.addAttribute("hasNext", farmLogPage.hasNext());
        model.addAttribute("farmLogs", farmLogPage.map(FarmLogDto::fromEntity).toList());
        model.addAttribute("pageNum",pageRequest.getPage());

        return "search.html";
    }

    private String getBackURI(HttpServletRequest request) {
        // 일기 상세화면에서 "뒤로가기" 버튼 구현
        if (request.getHeader("referer") == null) {
            // referer 없는 경우(직접 유입) : 뒤로가기로 home
            return "/";
        } else if (hasSameURI(
                request.getRequestURI(),
                request.getHeader("referer"))) {
            // referer == 현재 페이지 -> 뒤로가기로 기존 session 값
            return (String) httpSession.getAttribute("backURI");
        } else {
            // 다른 페이지를 타고온 경우(referer != 현재 페이지) -> save referer to session
            httpSession.setAttribute("backURI",
                    request.getHeader("referer"));
            return request.getHeader("referer");
        }
    }

    private Boolean hasSameURI(@NonNull String request,
                               String referer) {
        if (referer==null) {
            return false;
        }
        return referer.endsWith(request);
    }
}
