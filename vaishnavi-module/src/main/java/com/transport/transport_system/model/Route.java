package com.transport.transport_system.model;

import jakarta.persistence.*;

@Entity
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String source;
    private String destination;
    private double distance;

    public Long getId() { return id; }

    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }

    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }

    public double getDistance() { return distance; }
    public void setDistance(double distance) { this.distance = distance; }

    // Builder Pattern implementation
    public static class RouteBuilder {
        private String source;
        private String destination;
        private double distance;

        public RouteBuilder setSource(String source) {
            this.source = source;
            return this;
        }

        public RouteBuilder setDestination(String destination) {
            this.destination = destination;
            return this;
        }

        public RouteBuilder setDistance(double distance) {
            this.distance = distance;
            return this;
        }

        public Route build() {
            Route route = new Route();
            route.setSource(this.source);
            route.setDestination(this.destination);
            route.setDistance(this.distance);
            return route;
        }
    }
}