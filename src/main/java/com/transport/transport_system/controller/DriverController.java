package com.transport.transport_system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.transport.transport_system.model.DriverUser;
import com.transport.transport_system.model.User;
import com.transport.transport_system.repository.ScheduleRepository;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/driver")
public class DriverController {

    @Autowired
    private ScheduleRepository scheduleRepository;

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
            model.addAttribute("driver", (DriverUser) user);
        } else {
            model.addAttribute("driver", user);
        }

        // No schedules wired yet — template handles empty list gracefully
            model.addAttribute("schedules",
        scheduleRepository.findByDriverId(user.getId())
    );


        return "driver/home";
    }
}

