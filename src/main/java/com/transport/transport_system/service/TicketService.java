package com.transport.transport_system.service;

import com.transport.transport_system.factory.TicketFactory;
import com.transport.transport_system.model.Route;
import com.transport.transport_system.model.Ticket;
import com.transport.transport_system.repository.RouteRepository;
import com.transport.transport_system.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private RouteRepository routeRepository;

    @Autowired
    private TicketFactory ticketFactory;

    public Ticket generateTicket(Long routeId, String source, String destination, int passengerCount) {
        Route route = routeRepository.findById(routeId).orElse(null);
        Ticket ticket = ticketFactory.createTicket(route, source, destination, passengerCount);
        return ticketRepository.save(ticket);
    }

    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }
}
