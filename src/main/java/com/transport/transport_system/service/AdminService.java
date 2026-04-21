package com.transport.transport_system.service;

import com.transport.transport_system.model.Bus;
import com.transport.transport_system.model.Driver;
import com.transport.transport_system.model.Route;
import com.transport.transport_system.model.Schedule;
import com.transport.transport_system.model.Booking;
import com.transport.transport_system.repository.BookingRepository;
import com.transport.transport_system.repository.BusRepository;
import com.transport.transport_system.repository.DriverRepository;
import com.transport.transport_system.repository.RouteRepository;
import com.transport.transport_system.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    @Autowired private RouteRepository routeRepository;
    @Autowired private BusRepository busRepository;
    @Autowired private DriverRepository driverRepository;
    @Autowired private ScheduleRepository scheduleRepository;
    @Autowired private BookingRepository bookingRepository;

    // ── Routes ───────────────────────────────────────────────────────────────────

    public Route createRoute(String source, String destination, double distance) {
        Route route = new Route.RouteBuilder()
                .setSource(source)
                .setDestination(destination)
                .setDistance(distance)
                .build();
        return routeRepository.save(route);
    }

    public List<Route> getAllRoutes() { return routeRepository.findAll(); }

    // ── Buses ────────────────────────────────────────────────────────────────────

    public Bus createBus(String number, int capacity, Long routeId) {
        Route route = routeId != null ? routeRepository.findById(routeId).orElse(null) : null;
        Bus bus = new Bus.BusBuilder()
                .setNumber(number)
                .setCapacity(capacity)
                .setRoute(route)
                .build();
        return busRepository.save(bus);
    }

    public List<Bus> getAllBuses() { return busRepository.findAll(); }

    // ── Drivers ──────────────────────────────────────────────────────────────────

    public Driver createDriver(String name, String license) {
        Driver driver = new Driver();
        driver.setName(name);
        driver.setLicenseNumber(license);
        return driverRepository.save(driver);
    }

    public List<Driver> getAllDrivers() { return driverRepository.findAll(); }

    // ── Schedules / Journey Assignment ───────────────────────────────────────────

    /**
     * Assign a journey: links a bus + driver to a route with date/time details.
     */
    public Schedule assignJourney(Long busId, Long driverId, Long routeId,
                                  String travelDate, String departureTime, String arrivalTime) {
        Route route = routeId != null ? routeRepository.findById(routeId).orElse(null) : null;
        Schedule schedule = new Schedule.ScheduleBuilder()
                .setBusId(busId)
                .setDriverId(driverId)
                .setRoute(route)
                .setTravelDate(travelDate)
                .setDepartureTime(departureTime)
                .setArrivalTime(arrivalTime)
                .build();
        return scheduleRepository.save(schedule);
    }

    public Schedule createSchedule(Long busId, String travelDate, String departureTime, Long routeId) {
        Route route = routeId != null ? routeRepository.findById(routeId).orElse(null) : null;
        Schedule schedule = new Schedule.ScheduleBuilder()
                .setBusId(busId)
                .setTravelDate(travelDate)
                .setDepartureTime(departureTime)
                .setRoute(route)
                .build();
        return scheduleRepository.save(schedule);
    }

    public List<Schedule> getAllSchedules() { return scheduleRepository.findAll(); }

    // ── Bookings ─────────────────────────────────────────────────────────────────

    public List<Booking> getAllBookings() { return bookingRepository.findAll(); }
}