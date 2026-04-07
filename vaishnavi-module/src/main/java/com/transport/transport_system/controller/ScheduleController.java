package com.transport.transport_system.controller;

import com.transport.transport_system.model.Schedule;
import com.transport.transport_system.model.Route;
import com.transport.transport_system.service.ScheduleService;
import com.transport.transport_system.service.RouteService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/schedules")
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private RouteService routeService;

    @GetMapping("/search-routes")
    public String searchSchedules(@RequestParam(value = "source", required = false) String source,
                                  @RequestParam(value = "destination", required = false) String destination,
                                  HttpSession session, Model model) {
        if (session.getAttribute("user") == null) {
            return "redirect:/auth/login";
        }

        try {
            List<Schedule> schedules;
            if (source != null && !source.trim().isEmpty() && destination != null && !destination.trim().isEmpty()) {
                schedules = scheduleService.searchSchedules(source, destination);
            } else {
                schedules = scheduleService.getAllSchedules();
            }
            model.addAttribute("schedules", schedules);
            model.addAttribute("routes", routeService.getAllRoutes());
            model.addAttribute("source", source);
            model.addAttribute("destination", destination);
        } catch (Exception e) {
            e.printStackTrace(); // VERY IMPORTANT (see error in terminal)
        }

        return "search-route";
    }

    // VIEW ALL SCHEDULES (OPTIONAL PAGE)
    @GetMapping
    public String viewSchedules(Model model) {
        model.addAttribute("schedules", scheduleService.getAllSchedules());
        return "schedules";
    }

    // ADD SCHEDULE PAGE
    @GetMapping("/add")
    public String showForm(Model model) {
        model.addAttribute("schedule", new Schedule());
        model.addAttribute("routes", routeService.getAllRoutes());
        return "add-schedule";
    }

    // SAVE SCHEDULE
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