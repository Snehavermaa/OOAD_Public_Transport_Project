package com.transport.transport_system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.transport.transport_system.model.Stop;
import com.transport.transport_system.service.StopService;

@Controller
@RequestMapping("/stops")
public class StopController {

    @Autowired
    private StopService stopService;

    // ✅ SINGLE METHOD handles BOTH cases
    @GetMapping
    public String viewStops(@RequestParam(required = false) Long routeId, Model model) {

        if (routeId != null) {
            model.addAttribute("stops", stopService.getStopsByRoute(routeId));
        } else {
            model.addAttribute("stops", stopService.getAllStops());
        }

        return "stops";
    }

    @GetMapping("/add")
    public String showForm(Model model) {
        model.addAttribute("stop", new Stop());
        return "add-stop";
    }

    @PostMapping("/save")
    public String saveStop(@ModelAttribute Stop stop) {
        stopService.saveStop(stop);
        return "redirect:/stops";
    }
}