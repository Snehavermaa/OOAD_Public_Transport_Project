package com.transport.transport_system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.transport.transport_system.model.DriverUser;
import com.transport.transport_system.model.Schedule;
import com.transport.transport_system.model.Stop;
import com.transport.transport_system.model.User;
import com.transport.transport_system.service.ScheduleService;

import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/driver")
public class DriverController {

    @Autowired
    private ScheduleService scheduleService;

    @GetMapping("/home")
    public String driverHome(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/auth/login";
        }
        if (!"DRIVER".equals(user.getRole())) {
            return "redirect:/auth/login";
        }

        // instanceof check before casting — avoids ClassCastException
        if (user instanceof DriverUser) {
            DriverUser driver = (DriverUser) user;
            model.addAttribute("driverName", driver.getFirstName() + " " + driver.getLastName());
            model.addAttribute("licenseNumber", driver.getLicenseNumber());
        } else {
            model.addAttribute("driverName", user.getFirstName() + " " + user.getLastName());
            model.addAttribute("licenseNumber", "");
        }

        List<Schedule> schedules = scheduleService.getSchedulesForDriver(user.getEmail(), user.getId());
        Map<Long, List<Stop>> stopsByRouteId = scheduleService.getStopsByRouteIdForSchedules(schedules);
        model.addAttribute("schedules", schedules);
        model.addAttribute("stopsByRouteId", stopsByRouteId);
        model.addAttribute("driverId", user.getId());


        return "driver/home";
    }
}

