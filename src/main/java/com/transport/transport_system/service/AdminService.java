package com.transport.transport_system.service;

import com.transport.transport_system.model.Bus;
import com.transport.transport_system.model.Route;
import com.transport.transport_system.model.Schedule;
import com.transport.transport_system.model.Booking;
import com.transport.transport_system.model.User;
import com.transport.transport_system.model.Stop;
import com.transport.transport_system.repository.BookingRepository;
import com.transport.transport_system.repository.BusRepository;
import com.transport.transport_system.repository.RouteRepository;
import com.transport.transport_system.repository.ScheduleRepository;
import com.transport.transport_system.repository.UserRepository;
import com.transport.transport_system.repository.StopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AdminService {

    @Autowired private RouteRepository routeRepository;
    @Autowired private BusRepository busRepository;
    @Autowired private ScheduleRepository scheduleRepository;
    @Autowired private BookingRepository bookingRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private StopRepository stopRepository;

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

    public List<User> getAllDrivers() { return userRepository.findByRole("DRIVER"); }

    // ── Schedules / Journey Assignment ───────────────────────────────────────────

    /**
     * Assign a journey: links a bus + driver to a route with date/time details.
     */
    public Schedule assignJourney(Long busId, Long driverId, Long routeId,
                                  String travelDate, String departureTime, String arrivalTime) {
        Route route = routeId != null ? routeRepository.findById(routeId).orElse(null) : null;
        User driver = driverId != null ? userRepository.findById(driverId).orElse(null) : null;
        Schedule schedule = new Schedule.ScheduleBuilder()
                .setBusId(busId)
                .setDriverId(driverId)
                .setDriverEmail(driver != null ? driver.getEmail() : null)
                .setRoute(route)
                .setTravelDate(travelDate)
                .setDepartureTime(departureTime)
                .setArrivalTime(arrivalTime)
                .build();
        return scheduleRepository.save(schedule);
    }

    public Schedule createSchedule(Long busId, String travelDate, String departureTime, String arrivalTime, Long routeId, String driverEmail) {
        Route route = routeId != null ? routeRepository.findById(routeId).orElse(null) : null;
        User driver = driverEmail != null ? userRepository.findByEmail(driverEmail).orElse(null) : null;
        Schedule schedule = new Schedule.ScheduleBuilder()
                .setBusId(busId)
                .setDriverId(driver != null ? driver.getId() : null)
                .setDriverEmail(driverEmail)
                .setTravelDate(travelDate)
                .setDepartureTime(departureTime)
                .setArrivalTime(arrivalTime)
                .setRoute(route)
                .build();
        return scheduleRepository.save(schedule);
    }

    public Schedule createScheduleWithStops(Long busId,
                                            String travelDate,
                                            String departureTime,
                                            String arrivalTime,
                                            Long routeId,
                                            String driverEmail,
                                            List<String> stopNames,
                                            List<String> stopArrivalTimes,
                                            List<String> stopDepartureTimes) {
        Schedule schedule = createSchedule(busId, travelDate, departureTime, arrivalTime, routeId, driverEmail);
        if (schedule.getRoute() == null) {
            return schedule;
        }

        if (stopNames == null || stopArrivalTimes == null || stopDepartureTimes == null) {
            return schedule;
        }

        int count = Math.min(stopNames.size(), Math.min(stopArrivalTimes.size(), stopDepartureTimes.size()));
        for (int i = 0; i < count; i++) {
            String name = stopNames.get(i) != null ? stopNames.get(i).trim() : "";
            String arr = stopArrivalTimes.get(i) != null ? stopArrivalTimes.get(i).trim() : "";
            String dep = stopDepartureTimes.get(i) != null ? stopDepartureTimes.get(i).trim() : "";
            if (name.isBlank()) {
                continue;
            }
            Stop stop = new Stop();
            stop.setRoute(schedule.getRoute());
            stop.setStopName(name);
            stop.setArrivalTime(arr);
            stop.setDepartureTime(dep);
            stopRepository.save(stop);
        }
        return schedule;
    }

    public List<Schedule> getAllSchedules() { return scheduleRepository.findAll(); }

    // ── Bookings ─────────────────────────────────────────────────────────────────

    public List<Booking> getAllBookings() { return bookingRepository.findAll(); }

    public long countDrivers() {
        return userRepository.countByRole("DRIVER");
    }

    public List<Map<String, Object>> bookingCountsByRoute() {
        return bookingRepository.countBookingsGroupedByRoute().stream()
                .map(r -> Map.of(
                        "route", r[0] + " -> " + r[1],
                        "count", r[2]))
                .collect(Collectors.toList());
    }

    public List<Map<String, Object>> bookingCountsByBus() {
        return bookingRepository.countBookingsGroupedByBus().stream()
                .map(r -> Map.of(
                        "busId", r[0],
                        "count", r[1]))
                .collect(Collectors.toList());
    }
}