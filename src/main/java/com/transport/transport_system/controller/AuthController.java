package com.transport.transport_system.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.transport.transport_system.model.User;
import com.transport.transport_system.service.AuthService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    // ─── LOGIN ───────────────────────────────────────────────────────────────────

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        HttpSession session,
                        Model model) {

        Optional<User> userOpt = authService.authenticate(email, password);

        if (userOpt.isPresent()) {
            User loggedInUser = userOpt.get();
            session.setAttribute("user", loggedInUser);
            switch (loggedInUser.getRole()) {
                case "ADMIN":     return "redirect:/admin/home";
                case "DRIVER":    return "redirect:/driver/home";
                case "PASSENGER":
                default:          return "redirect:/user/home";
            }
        }

        model.addAttribute("error", "Invalid email or password. Please try again.");
        return "login";
    }

    // ─── REGISTER ────────────────────────────────────────────────────────────────

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
                           @RequestParam(defaultValue = "PASSENGER") String role,
                           @RequestParam(required = false) String address,
                           @RequestParam(required = false) String city,
                           @RequestParam(required = false) String zipCode,
                           @RequestParam(required = false) String licenseNumber,
                           Model model) {

        // Validate passwords match
        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match.");
            return "register";
        }

        // Validate password strength
        if (!authService.isPasswordStrong(password)) {
            model.addAttribute("error", "Password must be at least 6 characters and contain letters and numbers.");
            return "register";
        }

        try {
            authService.registerByRole(
                    role, email, password, firstName, lastName, phoneNumber,
                    address != null ? address : "",
                    city != null ? city : "",
                    zipCode != null ? zipCode : "",
                    licenseNumber != null ? licenseNumber : ""
            );

            // After successful registration, send them to login
            return "redirect:/auth/login?registered=true";

        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        } catch (Exception e) {
            model.addAttribute("error", "Registration failed. Please try again.");
            return "register";
        }
    }

    // ─── LOGOUT ──────────────────────────────────────────────────────────────────

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/auth/login";
    }
}