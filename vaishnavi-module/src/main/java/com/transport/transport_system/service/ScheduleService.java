package com.transport.transport_system.service;

import com.transport.transport_system.model.Schedule;
import com.transport.transport_system.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepository;

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
        return scheduleRepository.findAll().stream()
                .filter(schedule -> schedule.getRoute() != null)
                .filter(schedule -> schedule.getRoute().getSource() != null && schedule.getRoute().getDestination() != null)
                .filter(schedule -> schedule.getRoute().getSource().equalsIgnoreCase(source.trim()) && schedule.getRoute().getDestination().equalsIgnoreCase(destination.trim()))
                .toList();
    }
}