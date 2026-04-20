package com.transport.transport_system.controller;

import com.transport.transport_system.service.AdminService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/home")
    public String home(HttpSession session, Model model) {
        if (session.getAttribute("user") == null) return "redirect:/auth/login";
        model.addAttribute("routes", adminService.getAllRoutes());
        model.addAttribute("buses", adminService.getAllBuses());
        return "admin/home";
    }

    @GetMapping("/add-route")
    public String addRouteForm(HttpSession session) {
        if (session.getAttribute("user") == null) return "redirect:/auth/login";
        return "admin/add-route";
    }

    @PostMapping("/add-route")
    public String addRoute(@RequestParam String source,
                           @RequestParam String destination,
                           @RequestParam double distance,
                           HttpSession session) {
        if (session.getAttribute("user") == null) return "redirect:/auth/login";
        adminService.createRoute(source, destination, distance);
        return "redirect:/admin/home";
    }

    @GetMapping("/add-bus")
    public String addBusForm(HttpSession session, Model model) {
        if (session.getAttribute("user") == null) return "redirect:/auth/login";
        model.addAttribute("routes", adminService.getAllRoutes());
        return "admin/add-bus";
    }

    @PostMapping("/add-bus")
    public String addBus(@RequestParam String number,
                         @RequestParam int capacity,
                         @RequestParam(required = false) Long routeId,
                         HttpSession session) {
        if (session.getAttribute("user") == null) return "redirect:/auth/login";
        adminService.createBus(number, capacity, routeId);
        return "redirect:/admin/home";
    }
}
