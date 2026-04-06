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

    /**
     * Display login page
     */
    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    /**
     * Handle user login
     */
    @PostMapping("/login")
    public String handleLogin(@RequestParam String email, @RequestParam String password,
                             HttpSession session, Model model) {
        Optional<User> user = authService.authenticate(email, password);

        if (user.isPresent()) {
            session.setAttribute("user", user.get());
            session.setAttribute("userId", user.get().getId());
            session.setAttribute("userRole", user.get().getRole());
            session.setAttribute("userName", user.get().getFirstName() + " " + user.get().getLastName());

            // Redirect based on role
            if (authService.isAdmin(user.get())) {
                return "redirect:/admin/dashboard";
            } else {
                return "redirect:/user/dashboard";
            }
        } else {
            model.addAttribute("error", "Invalid email or password");
            return "login";
        }
    }

    /**
     * Display registration page
     */
    @GetMapping("/register")
    public String showRegistrationPage() {
        return "register";
    }

    /**
     * Handle user registration
     */
    @PostMapping("/register")
    public String handleRegistration(@RequestParam String email, 
                                    @RequestParam String password,
                                    @RequestParam String confirmPassword,
                                    @RequestParam String firstName,
                                    @RequestParam String lastName,
                                    @RequestParam String phoneNumber,
                                    @RequestParam(required = false) String address,
                                    @RequestParam(required = false) String city,
                                    @RequestParam(required = false) String zipCode,
                                    Model model) {
        // Validate email format
        if (!authService.isValidEmail(email)) {
            model.addAttribute("error", "Invalid email format");
            return "register";
        }

        // Validate password strength
        if (!authService.isPasswordStrong(password)) {
            model.addAttribute("error", "Password must be at least 6 characters with letters and numbers");
            return "register";
        }

        // Check if passwords match
        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match");
            return "register";
        }

        try {
            User newUser;
            if (address != null && !address.isEmpty() && city != null && !city.isEmpty()) {
                newUser = userService.registerUserWithAddress(email, password, firstName, lastName,
                        phoneNumber, address, city, zipCode);
            } else {
                newUser = userService.registerUser(email, password, firstName, lastName, phoneNumber);
            }

            model.addAttribute("success", "Registration successful! You can now login.");
            return "register";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }

    /**
     * Handle user logout
     */
    @GetMapping("/logout")
    public String handleLogout(HttpSession session) {
        session.invalidate();
        authService.logout();
        return "redirect:/auth/login";
    }

    /**
     * Display user dashboard
     */
    @GetMapping("/user/dashboard")
    public String showUserDashboard(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");

        if (user == null || !authService.isRegularUser(user)) {
            return "redirect:/auth/login";
        }

        model.addAttribute("user", user);
        return "user-dashboard";
    }

    /**
     * Display admin dashboard
     */
    @GetMapping("/admin/dashboard")
    public String showAdminDashboard(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");

        if (user == null || !authService.isAdmin(user)) {
            return "redirect:/auth/login";
        }

        model.addAttribute("user", user);
        model.addAttribute("totalUsers", userService.getAllUsers().size());
        model.addAttribute("adminCount", userService.getUsersByRole("ADMIN").size());
        model.addAttribute("userCount", userService.getUsersByRole("USER").size());

        return "admin-dashboard";
    }

    /**
     * REST API: Register user
     */
    @PostMapping("/api/register")
    @ResponseBody
    public Object registerUserApi(@RequestBody RegistrationRequest request, Model model) {
        try {
            if (!authService.isValidEmail(request.getEmail())) {
                return "{\"success\": false, \"message\": \"Invalid email format\"}";
            }

            if (!authService.isPasswordStrong(request.getPassword())) {
                return "{\"success\": false, \"message\": \"Password too weak\"}";
            }

            User newUser = userService.registerUser(request.getEmail(), request.getPassword(),
                    request.getFirstName(), request.getLastName(), request.getPhoneNumber());

            return "{\"success\": true, \"message\": \"User registered successfully\", \"userId\": " + newUser.getId() + "}";
        } catch (Exception e) {
            return "{\"success\": false, \"message\": \"" + e.getMessage() + "\"}";
        }
    }

    /**
     * REST API: Login user
     */
    @PostMapping("/api/login")
    @ResponseBody
    public Object loginUserApi(@RequestBody LoginRequest request) {
        Optional<User> user = authService.authenticate(request.getEmail(), request.getPassword());

        if (user.isPresent()) {
            User foundUser = user.get();
            return "{\"success\": true, \"message\": \"Login successful\", \"userId\": " + foundUser.getId() +
                    ", \"role\": \"" + foundUser.getRole() + "\"}";
        } else {
            return "{\"success\": false, \"message\": \"Invalid credentials\"}";
        }
    }

    // Helper classes for API requests
    public static class RegistrationRequest {
        private String email;
        private String password;
        private String firstName;
        private String lastName;
        private String phoneNumber;

        // Constructors
        public RegistrationRequest() {}

        public RegistrationRequest(String email, String password, String firstName,
                                   String lastName, String phoneNumber) {
            this.email = email;
            this.password = password;
            this.firstName = firstName;
            this.lastName = lastName;
            this.phoneNumber = phoneNumber;
        }

        // Getters and Setters
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }

        public String getFirstName() { return firstName; }
        public void setFirstName(String firstName) { this.firstName = firstName; }

        public String getLastName() { return lastName; }
        public void setLastName(String lastName) { this.lastName = lastName; }

        public String getPhoneNumber() { return phoneNumber; }
        public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    }

    public static class LoginRequest {
        private String email;
        private String password;

        // Constructors
        public LoginRequest() {}

        public LoginRequest(String email, String password) {
            this.email = email;
            this.password = password;
        }

        // Getters and Setters
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }
}
