package com.transport.transport_system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    /**
     * Display home page
     */
    @GetMapping("/")
    public String home() {
        return "index";
    }

    /**
     * Redirect to home if accessing root with slash redirect
     */
    @GetMapping("")
    public String rootRedirect() {
        return "redirect:/";
    }
}
