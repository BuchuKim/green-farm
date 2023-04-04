package com.buchu.greenfarm.controller;

import com.buchu.greenfarm.dto.farmLog.CreateFarmLog;
import com.buchu.greenfarm.service.FarmLogService;
import com.buchu.greenfarm.util.PageRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {
    private final FarmLogService farmLogService;

    @GetMapping("/")
    public String getHome(Model model,
                          @RequestParam(value = "following",
                                     required = false,
                                     defaultValue = "false")
                             final Boolean following,
                          final PageRequest pageRequest) {
        model.addAttribute("createFarmLog", new CreateFarmLog.Request());
        Map<String, Object> data = farmLogService.getAllFarmLogsPage(pageRequest.of(),following);
        model.addAttribute("pageNum", pageRequest.getPage());
        model.addAttribute("following", following);
        model.addAttribute("hasNext", data.get("hasNext"));
        model.addAttribute("farmLogs", data.get("farmLogs"));
        return "index.html";
    }

    @GetMapping("/i")
    public String getInfoPage() {
        return "info.html";
    }


    @GetMapping("/favicon.ico")
    @ResponseBody
    public void returnNoFavicon() {}
}
