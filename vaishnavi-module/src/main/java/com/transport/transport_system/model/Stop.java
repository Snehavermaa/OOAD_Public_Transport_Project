package com.transport.transport_system.model;

import jakarta.persistence.*;

@Entity
public class Stop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String stopName;
    private String arrivalTime;
    private String departureTime;

    @ManyToOne
    @JoinColumn(name = "route_id")
    private Route route;

    public String getStopName() { return stopName; }
    public void setStopName(String stopName) { this.stopName = stopName; }

    public String getArrivalTime() { return arrivalTime; }
    public void setArrivalTime(String arrivalTime) { this.arrivalTime = arrivalTime; }

    public String getDepartureTime() { return departureTime; }
    public void setDepartureTime(String departureTime) { this.departureTime = departureTime; }

    public Route getRoute() { return route; }
    public void setRoute(Route route) { this.route = route; }
}