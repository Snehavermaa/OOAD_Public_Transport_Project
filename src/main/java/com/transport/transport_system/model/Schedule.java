package com.transport.transport_system.model;

import jakarta.persistence.*;

@Entity
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long busId;
    private String travelDate;
    private String departureTime;

    @ManyToOne
    @JoinColumn(name = "route_id")
    private Route route;

    public Long getId() { return id; }

    public Long getBusId() { return busId; }
    public void setBusId(Long busId) { this.busId = busId; }

    public String getTravelDate() { return travelDate; }
    public void setTravelDate(String travelDate) { this.travelDate = travelDate; }

    public String getDepartureTime() { return departureTime; }
    public void setDepartureTime(String departureTime) { this.departureTime = departureTime; }

    public Route getRoute() { return route; }
    public void setRoute(Route route) { this.route = route; }

    // Builder Pattern implementation
    public static class ScheduleBuilder {
        private Long busId;
        private String travelDate;
        private String departureTime;
        private Route route;

        public ScheduleBuilder setBusId(Long busId) {
            this.busId = busId;
            return this;
        }

        public ScheduleBuilder setTravelDate(String travelDate) {
            this.travelDate = travelDate;
            return this;
        }

        public ScheduleBuilder setDepartureTime(String departureTime) {
            this.departureTime = departureTime;
            return this;
        }

        public ScheduleBuilder setRoute(Route route) {
            this.route = route;
            return this;
        }

        public Schedule build() {
            Schedule schedule = new Schedule();
            schedule.setBusId(this.busId);
            schedule.setTravelDate(this.travelDate);
            schedule.setDepartureTime(this.departureTime);
            schedule.setRoute(this.route);
            return schedule;
        }
    }
}