package com.transport.transport_system.controller;

import com.transport.transport_system.model.Route;
import com.transport.transport_system.model.Ticket;
import com.transport.transport_system.service.RouteService;
import com.transport.transport_system.service.TicketService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/conductor")
public class ConductorController {

    @Autowired
    private RouteService routeService;

    @Autowired
    private TicketService ticketService;

    @GetMapping("/home")
    public String home(HttpSession session, Model model) {
        if (session.getAttribute("user") == null) return "redirect:/auth/login";
        List<Route> routes = routeService.getAllRoutes();
        model.addAttribute("routes", routes);
        return "conductor/home";
    }

    @PostMapping("/issue-ticket")
    public String issueTicket(@RequestParam Long routeId,
                              @RequestParam String source,
                              @RequestParam String destination,
                              @RequestParam int passengerCount,
                              HttpSession session, Model model) {
        if (session.getAttribute("user") == null) return "redirect:/auth/login";

        Ticket ticket = ticketService.generateTicket(routeId, source, destination, passengerCount);
        model.addAttribute("ticket", ticket);
        return "conductor/ticket-summary";
    }
}
