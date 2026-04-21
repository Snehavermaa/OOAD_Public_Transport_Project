package com.transport.transport_system.controller;

import com.transport.transport_system.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ReportController {

    private final ReportService reportService;

    @Autowired
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/reports")
    public String displayReports(Model model) {
        model.addAttribute("bookings", reportService.getBookingHistory());
        model.addAttribute("transactions", reportService.getTransactionHistory());
        model.addAttribute("totalRevenue", reportService.getTotalRevenue());
        model.addAttribute("revenueBreakdown", reportService.getRevenueByPaymentMethod());
        return "reports";
    }
}
