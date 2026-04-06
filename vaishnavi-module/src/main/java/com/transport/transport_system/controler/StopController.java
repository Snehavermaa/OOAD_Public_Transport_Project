package com.transport.transport_system.controller;

import com.transport.transport_system.model.Stop;
import com.transport.transport_system.service.StopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/stops")
public class StopController {

    @Autowired
    private StopService stopService;

    @GetMapping
    public String viewStops(Model model) {
        model.addAttribute("stops", stopService.getAllStops());
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