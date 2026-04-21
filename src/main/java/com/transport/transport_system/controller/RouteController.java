package com.transport.transport_system.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.transport.transport_system.service.RouteService;

import jakarta.servlet.http.HttpSession;

@Controller
public class RouteController {

    @Autowired
    private RouteService routeService;

    @GetMapping("/routes/search-routes")
    public String searchRoutes(HttpSession session, Model model) {

        // if (session.getAttribute("user") == null) {
        //     return "redirect:/auth/login";
        // }

        // ✅ Send BOTH variables (VERY IMPORTANT)
        model.addAttribute("routes", routeService.getAllRoutes());
        model.addAttribute("schedules", new ArrayList<>());

        return "search-route";
    }
}