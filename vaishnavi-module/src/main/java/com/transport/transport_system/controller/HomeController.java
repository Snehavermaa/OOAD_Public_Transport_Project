package com.transport.transport_system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/start")
    public String start() {
        return "redirect:/auth/register";
    }
}