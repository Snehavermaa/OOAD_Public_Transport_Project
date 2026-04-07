package com.transport.transport_system.controller;

import com.transport.transport_system.model.User;
import com.transport.transport_system.service.AuthService;
import com.transport.transport_system.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

    // ===== LOGIN =====
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        HttpSession session,
                        Model model) {

        Optional<User> user = authService.authenticate(email, password);

        if (user.isPresent()) {
            session.setAttribute("user", user.get());

            // 🔥 GO TO BUS PAGE
            return "redirect:/schedules/search-routes";
        }

        model.addAttribute("error", "Invalid credentials");
        return "login";
    }

    // ===== REGISTER =====
    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String email,
                           @RequestParam String password,
                           @RequestParam String confirmPassword,
                           @RequestParam String firstName,
                           @RequestParam String lastName,
                           @RequestParam String phoneNumber,
                           Model model) {

        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match");
            return "register";
        }

        try {
            userService.registerUser(email, password, firstName, lastName, phoneNumber);

            // 🔥 AFTER REGISTER → LOGIN
            return "redirect:/auth/login";

        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }

    // ===== LOGOUT =====
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/auth/login";
    }
}