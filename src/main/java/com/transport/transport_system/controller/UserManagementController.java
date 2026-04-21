package com.transport.transport_system.controller;

import com.transport.transport_system.model.Admin;
import com.transport.transport_system.model.User;
import com.transport.transport_system.service.AuthService;
import com.transport.transport_system.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/users")
public class UserManagementController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

    /**
     * Display list of all users (Admin only)
     */
    @GetMapping("")
    public String listUsers(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");

        if (user == null || !authService.isAdmin(user)) {
            return "redirect:/auth/login";
        }

        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);

        return "user-management";
    }

    /**
     * Display list of admins (Admin only)
     */
    @GetMapping("/admins")
    public String listAdmins(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");

        if (user == null || !authService.isAdmin(user)) {
            return "redirect:/auth/login";
        }

        List<User> admins = userService.getUsersByRole("ADMIN");
        model.addAttribute("admins", admins);

        return "admin-list";
    }

    /**
     * Display list of regular users (Admin only)
     */
    @GetMapping("/regular")
    public String listRegularUsers(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");

        if (user == null || !authService.isAdmin(user)) {
            return "redirect:/auth/login";
        }

        List<User> regularUsers = userService.getUsersByRole("USER");
        model.addAttribute("regularUsers", regularUsers);

        return "regular-user-list";
    }

    /**
     * Display user details
     */
    @GetMapping("/{id}")
    public String viewUserDetails(@PathVariable Long id, HttpSession session, Model model) {
        User currentUser = (User) session.getAttribute("user");

        if (currentUser == null) {
            return "redirect:/auth/login";
        }

        Optional<User> user = userService.getUserById(id);
        if (user.isEmpty()) {
            return "redirect:/admin/users";
        }

        model.addAttribute("user", user.get());

        return "user-details";
    }

    /**
     * Show form to register new admin (Admin only)
     */
    @GetMapping("/create-admin")
    public String showCreateAdminForm(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");

        if (user == null || !authService.isAdmin(user)) {
            return "redirect:/auth/login";
        }

        return "create-admin";
    }

    /**
     * Register new admin (Admin only)
     */
    @PostMapping("/create-admin")
    public String createAdmin(@RequestParam String email,
                             @RequestParam String password,
                             @RequestParam String firstName,
                             @RequestParam String lastName,
                             @RequestParam String phoneNumber,
                             @RequestParam String department,
                             @RequestParam String permissions,
                             HttpSession session,
                             Model model) {
        User currentUser = (User) session.getAttribute("user");

        if (currentUser == null || !authService.isAdmin(currentUser)) {
            return "redirect:/auth/login";
        }

        try {
            if (!authService.isValidEmail(email)) {
                model.addAttribute("error", "Invalid email format");
                return "create-admin";
            }

            if (!authService.isPasswordStrong(password)) {
                model.addAttribute("error", "Password must be at least 6 characters with letters and numbers");
                return "create-admin";
            }

            Admin newAdmin = userService.registerAdmin(email, password, firstName, lastName,
                    phoneNumber, department, permissions);

            model.addAttribute("success", "Admin created successfully");
            model.addAttribute("adminId", newAdmin.getId());

            return "redirect:/admin/users/admins";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "create-admin";
        }
    }

    /**
     * Update user information (Admin or user updating their own info)
     */
    @PostMapping("/{id}/update")
    public String updateUser(@PathVariable Long id,
                            @RequestParam String firstName,
                            @RequestParam String lastName,
                            @RequestParam String phoneNumber,
                            HttpSession session,
                            Model model) {
        User currentUser = (User) session.getAttribute("user");

        if (currentUser == null) {
            return "redirect:/auth/login";
        }

        // Allow user to update their own info or admin to update any user
        if (!authService.isAdmin(currentUser) && !currentUser.getId().equals(id)) {
            return "redirect:/auth/login";
        }

        try {
            User updatedUser = userService.updateUser(id, firstName, lastName, phoneNumber);
            model.addAttribute("success", "User updated successfully");

            return "redirect:/admin/users/" + id;
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());

            return "redirect:/admin/users";
        }
    }

    /**
     * Deactivate user account
     */
    @PostMapping("/{id}/deactivate")
    public String deactivateUser(@PathVariable Long id, HttpSession session, Model model) {
        User currentUser = (User) session.getAttribute("user");

        if (currentUser == null || !authService.isAdmin(currentUser)) {
            return "redirect:/auth/login";
        }

        try {
            userService.deactivateUser(id);
            model.addAttribute("success", "User deactivated successfully");

            return "redirect:/admin/users";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());

            return "redirect:/admin/users";
        }
    }

    /**
     * Activate user account
     */
    @PostMapping("/{id}/activate")
    public String activateUser(@PathVariable Long id, HttpSession session, Model model) {
        User currentUser = (User) session.getAttribute("user");

        if (currentUser == null || !authService.isAdmin(currentUser)) {
            return "redirect:/auth/login";
        }

        try {
            userService.activateUser(id);
            model.addAttribute("success", "User activated successfully");

            return "redirect:/admin/users";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());

            return "redirect:/admin/users";
        }
    }

    /**
     * Delete user
     */
    @PostMapping("/{id}/delete")
    public String deleteUser(@PathVariable Long id, HttpSession session, Model model) {
        User currentUser = (User) session.getAttribute("user");

        if (currentUser == null || !authService.isAdmin(currentUser)) {
            return "redirect:/auth/login";
        }

        try {
            userService.deleteUser(id);
            model.addAttribute("success", "User deleted successfully");

            return "redirect:/admin/users";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());

            return "redirect:/admin/users";
        }
    }
}
