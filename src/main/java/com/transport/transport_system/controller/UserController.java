package com.transport.transport_system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {

    @GetMapping("/home")
    public String userHome(HttpSession session, Model model) {
        if (session.getAttribute("user") == null) return "redirect:/auth/login";
        return "user/home";
    }
}
