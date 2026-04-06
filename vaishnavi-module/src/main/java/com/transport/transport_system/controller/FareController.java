package com.transport.transport_system.controller;

import com.transport.transport_system.model.Route;
import com.transport.transport_system.service.FareService;
import com.transport.transport_system.service.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class FareController {

    @Autowired
    private FareService fareService;

    @Autowired
    private RouteService routeService;

    @GetMapping("/fare")
    public String showFarePage(Model model) {
        model.addAttribute("routes", routeService.getAllRoutes());
        return "fare";
    }

    @PostMapping("/calculateFare")
    public String calculateFare(@RequestParam Long routeId,
                                @RequestParam(required = false) Boolean isAC,
                                @RequestParam(required = false) Boolean isPeak,
                                Model model) {

        Route route = routeService.getAllRoutes()
                .stream()
                .filter(r -> r.getId().equals(routeId))
                .findFirst()
                .orElse(null);

        boolean ac = (isAC != null);
        boolean peak = (isPeak != null);

        double fare = fareService.calculateFare(route.getDistance(), ac, peak);

        model.addAttribute("fare", fare);
        model.addAttribute("routes", routeService.getAllRoutes());

        return "fare";
    }
}