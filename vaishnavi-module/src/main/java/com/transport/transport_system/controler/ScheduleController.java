package com.transport.transport_system.controller;

import com.transport.transport_system.model.Schedule;
import com.transport.transport_system.model.Route;
import com.transport.transport_system.service.ScheduleService;
import com.transport.transport_system.service.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/schedules")
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private RouteService routeService;

    @GetMapping
    public String viewSchedules(Model model) {
        model.addAttribute("schedules", scheduleService.getAllSchedules());
        return "schedules";
    }

    @GetMapping("/add")
    public String showForm(Model model) {
        model.addAttribute("schedule", new Schedule());
        model.addAttribute("routes", routeService.getAllRoutes());
        return "add-schedule";
    }

    @PostMapping("/save")
    public String saveSchedule(@ModelAttribute Schedule schedule,
                               @RequestParam("route.id") Long routeId) {

        Route route = routeService.getAllRoutes()
                .stream()
                .filter(r -> r.getId().equals(routeId))
                .findFirst()
                .orElse(null);

        schedule.setRoute(route);

        scheduleService.saveSchedule(schedule);

        return "redirect:/schedules";
    }
}