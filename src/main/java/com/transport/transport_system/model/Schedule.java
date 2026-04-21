package com.transport.transport_system.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long busId;
    private Long driverId;       // links to DriverUser.id
    private String driverEmail;
    private String travelDate;
    private String departureTime;
    private String arrivalTime;

    @ManyToOne
    @JoinColumn(name = "route_id")
    private Route route;

    public Long getId() { return id; }

    public Long getBusId() { return busId; }
    public void setBusId(Long busId) { this.busId = busId; }

    public Long getDriverId() { return driverId; }
    public void setDriverId(Long driverId) { this.driverId = driverId; }

    public String getDriverEmail() { return driverEmail; }
    public void setDriverEmail(String driverEmail) { this.driverEmail = driverEmail; }

    public String getTravelDate() { return travelDate; }
    public void setTravelDate(String travelDate) { this.travelDate = travelDate; }

    public String getDepartureTime() { return departureTime; }
    public void setDepartureTime(String departureTime) { this.departureTime = departureTime; }

    public String getArrivalTime() { return arrivalTime; }
    public void setArrivalTime(String arrivalTime) { this.arrivalTime = arrivalTime; }

    public Route getRoute() { return route; }
    public void setRoute(Route route) { this.route = route; }

    // ── Builder ──────────────────────────────────────────────────────────────────
    public static class ScheduleBuilder {
        private Long busId;
        private Long driverId;
        private String driverEmail;
        private String travelDate;
        private String departureTime;
        private String arrivalTime;
        private Route route;

        public ScheduleBuilder setBusId(Long busId) { this.busId = busId; return this; }
        public ScheduleBuilder setDriverId(Long driverId) { this.driverId = driverId; return this; }
        public ScheduleBuilder setDriverEmail(String driverEmail) { this.driverEmail = driverEmail; return this; }
        public ScheduleBuilder setTravelDate(String travelDate) { this.travelDate = travelDate; return this; }
        public ScheduleBuilder setDepartureTime(String departureTime) { this.departureTime = departureTime; return this; }
        public ScheduleBuilder setArrivalTime(String arrivalTime) { this.arrivalTime = arrivalTime; return this; }
        public ScheduleBuilder setRoute(Route route) { this.route = route; return this; }

        public Schedule build() {
            Schedule s = new Schedule();
            s.setBusId(this.busId);
            s.setDriverId(this.driverId);
            s.setDriverEmail(this.driverEmail);
            s.setTravelDate(this.travelDate);
            s.setDepartureTime(this.departureTime);
            s.setArrivalTime(this.arrivalTime);
            s.setRoute(this.route);
            return s;
        }
    }
}