package com.transport.transport_system.controller;

import com.transport.transport_system.model.Route;
import com.transport.transport_system.service.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/routes")
public class RouteController {

    @Autowired
    private RouteService routeService;

    @GetMapping
    public String viewRoutes(Model model) {
        model.addAttribute("routes", routeService.getAllRoutes());
        return "routes";
    }

    @GetMapping("/add")
    public String showForm(Model model) {
        model.addAttribute("route", new Route());
        return "add-route";
    }

    @PostMapping("/save")
    public String saveRoute(@ModelAttribute Route route) {
        routeService.saveRoute(route);
        return "redirect:/routes";
    }
}