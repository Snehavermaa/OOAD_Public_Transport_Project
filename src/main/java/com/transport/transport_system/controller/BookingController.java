package com.transport.transport_system.controller;

import com.transport.transport_system.model.Booking;
import com.transport.transport_system.model.Schedule;
import com.transport.transport_system.model.User;
import com.transport.transport_system.service.BookingService;
import com.transport.transport_system.service.FareService;
import com.transport.transport_system.service.ScheduleService;
import jakarta.servlet.http.HttpSession;
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
    @Autowired
    private FareService fareService;

    @GetMapping("/")
    public String home() {
        return "redirect:/search-routes";
    }

    @GetMapping("/search-routes")
    public String showSearchForm(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }
        java.util.List<Schedule> schedules = scheduleService.getAllSchedules();
        model.addAttribute("source", "");
        model.addAttribute("destination", "");
        model.addAttribute("schedules", schedules);
        model.addAttribute("stopsByRouteId", scheduleService.getStopsByRouteIdForSchedules(schedules));
        return "search-route";
    }

    @PostMapping("/search-routes")
    public String searchRoutes(@RequestParam String source,
                               @RequestParam String destination,
                               Model model,
                               HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }
        java.util.List<Schedule> schedules = scheduleService.searchSchedules(source, destination);
        model.addAttribute("source", source);
        model.addAttribute("destination", destination);
        model.addAttribute("schedules", schedules);
        model.addAttribute("stopsByRouteId", scheduleService.getStopsByRouteIdForSchedules(schedules));
        return "search-route";
    }

    @GetMapping("/bookings/new")
    public String showBookingForm(@RequestParam Long scheduleId, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }
        Schedule schedule = scheduleService.getScheduleById(scheduleId);
        double farePerSeat = 0.0;
        if (schedule != null && schedule.getRoute() != null) {
            farePerSeat = fareService.calculateBaseFarePerSeat(schedule.getRoute().getDistance());
        }
        double taxPerSeat = fareService.calculateTax(farePerSeat);
        double totalPerSeat = fareService.calculateTotalWithTax(farePerSeat);
        model.addAttribute("booking", new Booking());
        model.addAttribute("schedule", schedule);
        model.addAttribute("farePerSeat", farePerSeat);
        model.addAttribute("taxPerSeat", taxPerSeat);
        model.addAttribute("totalPerSeat", totalPerSeat);
        model.addAttribute("taxRatePercent", (int) (fareService.getTaxRate() * 100));
        return "booking-form";
    }

    @PostMapping("/bookings/create")
    public String createBooking(@ModelAttribute Booking booking,
                                @RequestParam Long scheduleId,
                                HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }
        booking.setPassengerEmail(user.getEmail());
        Booking savedBooking = bookingService.createBooking(booking, scheduleId);
        return "redirect:/payment/new?bookingId=" + savedBooking.getId();
    }

    @GetMapping("/bookings")
    public String viewBookings(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }
        model.addAttribute("bookings", bookingService.getBookingsByUserEmail(user.getEmail()));
        return "bookings";
    }

    @PostMapping("/bookings/cancel")
    public String cancelBooking(@RequestParam Long bookingId) {
        bookingService.cancelBooking(bookingId);
        return "redirect:/bookings";
    }
}