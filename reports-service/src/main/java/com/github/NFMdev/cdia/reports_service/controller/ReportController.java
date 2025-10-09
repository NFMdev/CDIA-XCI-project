package com.github.NFMdev.cdia.reports_service.controller;

import com.github.NFMdev.cdia.reports_service.service.ReportService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model = reportService.getDashboardData(model);

        return "dashboard"; // Thymeleaf template
    }
}
