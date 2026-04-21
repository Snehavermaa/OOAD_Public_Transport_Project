package com.transport.transport_system.service;

import com.transport.transport_system.model.Schedule;
import com.transport.transport_system.model.Stop;
import com.transport.transport_system.repository.ScheduleRepository;
import com.transport.transport_system.repository.StopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepository;
    @Autowired
    private StopRepository stopRepository;

    public Schedule saveSchedule(Schedule schedule) {
        return scheduleRepository.save(schedule);
    }

    public List<Schedule> getAllSchedules() {
        return scheduleRepository.findAll();
    }

    public Schedule getScheduleById(Long id) {
        return scheduleRepository.findById(id).orElse(null);
    }

    public List<Schedule> searchSchedules(String source, String destination) {
        String sourceNormalized = source == null ? "" : source.trim().toLowerCase();
        String destinationNormalized = destination == null ? "" : destination.trim().toLowerCase();
        return scheduleRepository.findAll().stream()
                .filter(schedule -> schedule.getRoute() != null)
                .filter(schedule -> schedule.getRoute().getSource() != null && schedule.getRoute().getDestination() != null)
                .filter(schedule -> schedule.getRoute().getSource().trim().toLowerCase().contains(sourceNormalized))
                .filter(schedule -> schedule.getRoute().getDestination().trim().toLowerCase().contains(destinationNormalized))
                .toList();
    }

    public Map<Long, List<Stop>> getStopsByRouteIdForSchedules(List<Schedule> schedules) {
        Map<Long, List<Stop>> stopsByRouteId = new HashMap<>();
        for (Schedule schedule : schedules) {
            if (schedule.getRoute() != null && schedule.getRoute().getId() != null) {
                Long routeId = schedule.getRoute().getId();
                stopsByRouteId.putIfAbsent(routeId, stopRepository.findByRouteId(routeId));
            }
        }
        return stopsByRouteId;
    }

    public List<Schedule> getSchedulesForDriver(String driverEmail, Long driverId) {
        if (driverEmail != null && !driverEmail.isBlank()) {
            List<Schedule> byEmail = scheduleRepository.findByDriverEmail(driverEmail);
            if (!byEmail.isEmpty()) {
                return byEmail;
            }
        }
        if (driverId != null) {
            return scheduleRepository.findByDriverId(driverId);
        }
        return Collections.emptyList();
    }
}