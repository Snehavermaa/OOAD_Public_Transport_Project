package com.transport.transport_system.factory;

import com.transport.transport_system.model.Route;
import com.transport.transport_system.model.Ticket;
import org.springframework.stereotype.Component;

@Component
public class TicketFactory {

    // Basic internal logic to simulate fare calculation based on passengers and a base rate
    public Ticket createTicket(Route route, String source, String destination, int passengerCount) {
        Ticket ticket = new Ticket();
        ticket.setRoute(route);
        ticket.setSource(source);
        ticket.setDestination(destination);
        ticket.setPassengerCount(passengerCount);

        // Calculate fare (simulate a basic fare of 15 rupees per passenger)
        // In a real system, distance between stops would be evaluated.
        double baseFarePerPassenger = 15.0; 
        
        if (route != null && route.getDistance() > 0) {
             // For example, 2 rupees per km per passenger
             baseFarePerPassenger = route.getDistance() * 2.0;
        }

        ticket.setFare(baseFarePerPassenger * passengerCount);
        
        return ticket;
    }
}
