package com.transport.transport_system.model;

import jakarta.persistence.*;

@Entity
public class Bus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String number;
    private int capacity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "route_id")
    private Route route;

    // Default constructor
    public Bus() {}

    private Bus(BusBuilder builder) {
        this.number = builder.number;
        this.capacity = builder.capacity;
        this.route = builder.route;
    }

    public Long getId() { return id; }
    public String getNumber() { return number; }
    public int getCapacity() { return capacity; }
    public Route getRoute() { return route; }

    public void setNumber(String number) { this.number = number; }
    public void setCapacity(int capacity) { this.capacity = capacity; }
    public void setRoute(Route route) { this.route = route; }

    // Builder Pattern implementation
    public static class BusBuilder {
        private String number;
        private int capacity;
        private Route route;

        public BusBuilder setNumber(String number) {
            this.number = number;
            return this;
        }

        public BusBuilder setCapacity(int capacity) {
            this.capacity = capacity;
            return this;
        }

        public BusBuilder setRoute(Route route) {
            this.route = route;
            return this;
        }

        public Bus build() {
            return new Bus(this);
        }
    }
}
