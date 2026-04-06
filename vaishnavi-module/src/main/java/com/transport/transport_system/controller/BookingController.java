package com.transport.transport_system.controller;

import com.transport.transport_system.model.Booking;
import com.transport.transport_system.model.Schedule;
import com.transport.transport_system.service.BookingService;
import com.transport.transport_system.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class BookingController {

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private BookingService bookingService;

    @GetMapping("/")
    public String home() {
        return "redirect:/search-routes";
    }

    @GetMapping("/search-routes")
    public String showSearchForm(Model model) {
        model.addAttribute("source", "");
        model.addAttribute("destination", "");
        model.addAttribute("schedules", scheduleService.getAllSchedules());
        return "search-route";
    }

    @PostMapping("/search-routes")
    public String searchRoutes(@RequestParam String source,
                               @RequestParam String destination,
                               Model model) {
        model.addAttribute("source", source);
        model.addAttribute("destination", destination);
        model.addAttribute("schedules", scheduleService.searchSchedules(source, destination));
        return "search-route";
    }

    @GetMapping("/bookings/new")
    public String showBookingForm(@RequestParam Long scheduleId, Model model) {
        Schedule schedule = scheduleService.getScheduleById(scheduleId);
        model.addAttribute("booking", new Booking());
        model.addAttribute("schedule", schedule);
        return "booking-form";
    }

    @PostMapping("/bookings/create")
    public String createBooking(@ModelAttribute Booking booking,
                                @RequestParam Long scheduleId) {
        bookingService.createBooking(booking, scheduleId);
        return "redirect:/bookings";
    }

    @GetMapping("/bookings")
    public String viewBookings(Model model) {
        model.addAttribute("bookings", bookingService.getAllBookings());
        return "bookings";
    }

    @PostMapping("/bookings/cancel")
    public String cancelBooking(@RequestParam Long bookingId) {
        bookingService.cancelBooking(bookingId);
        return "redirect:/bookings";
    }
}