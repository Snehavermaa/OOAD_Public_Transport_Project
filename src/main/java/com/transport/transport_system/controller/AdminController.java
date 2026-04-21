package com.transport.transport_system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.transport.transport_system.model.User;
import com.transport.transport_system.service.AdminService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    // ── Guard helper ─────────────────────────────────────────────────────────────
    private boolean isAdmin(HttpSession session) {
        User user = (User) session.getAttribute("user");
        return user != null && "ADMIN".equals(user.getRole());
    }

    // ── Dashboard / Home ─────────────────────────────────────────────────────────

    @GetMapping({"", "/", "/home"})   // handles /admin, /admin/, /admin/home
    public String home(HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/auth/login";
        if (adminService.getAllDrivers().isEmpty()) {
            adminService.createDriver("Vaishnavi Reddy", "DL12345");
        }
        model.addAttribute("routes",    adminService.getAllRoutes());
        model.addAttribute("buses",     adminService.getAllBuses());
        model.addAttribute("drivers",   adminService.getAllDrivers());
        model.addAttribute("schedules", adminService.getAllSchedules());
        model.addAttribute("bookings",  adminService.getAllBookings());
        return "admin/home";
    }

    // ── Add Route ────────────────────────────────────────────────────────────────

    @GetMapping("/add-route")
    public String addRouteForm(HttpSession session) {
        if (!isAdmin(session)) return "redirect:/auth/login";
        return "admin/add-route";
    }

    @PostMapping("/add-route")
    public String addRoute(@RequestParam String source,
                           @RequestParam String destination,
                           @RequestParam double distance,
                           HttpSession session) {
        if (!isAdmin(session)) return "redirect:/auth/login";
        adminService.createRoute(source, destination, distance);
        return "redirect:/admin/home";
    }

    // ── Add Bus ──────────────────────────────────────────────────────────────────

    @GetMapping("/add-bus")
    public String addBusForm(HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/auth/login";
        model.addAttribute("routes", adminService.getAllRoutes());
        return "admin/add-bus";
    }

    @PostMapping("/add-bus")
    public String addBus(@RequestParam String number,
                         @RequestParam int capacity,
                         @RequestParam(required = false) Long routeId,
                         HttpSession session) {
        if (!isAdmin(session)) return "redirect:/auth/login";
        adminService.createBus(number, capacity, routeId);
        return "redirect:/admin/home";
    }

    // ── Assign Journey (bus + driver + schedule) ─────────────────────────────────

    @GetMapping("/assign-journey")
    public String assignJourneyForm(HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/auth/login";
        model.addAttribute("buses",   adminService.getAllBuses());
        model.addAttribute("drivers", adminService.getAllDrivers());
        model.addAttribute("routes",  adminService.getAllRoutes());
        return "admin/assign-journey";
    }

    @PostMapping("/assign-journey")
    public String assignJourney(@RequestParam Long busId,
                                @RequestParam Long driverId,
                                @RequestParam Long routeId,
                                @RequestParam String travelDate,
                                @RequestParam String departureTime,
                                @RequestParam String arrivalTime,
                                HttpSession session) {
        if (!isAdmin(session)) return "redirect:/auth/login";
        adminService.assignJourney(busId, driverId, routeId, travelDate, departureTime, arrivalTime);
        return "redirect:/admin/home";
    }

    // ── Bookings view ────────────────────────────────────────────────────────────

    @GetMapping("/bookings")
    public String viewBookings(HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/auth/login";
        model.addAttribute("bookings", adminService.getAllBookings());
        return "admin/bookings";
    }
}