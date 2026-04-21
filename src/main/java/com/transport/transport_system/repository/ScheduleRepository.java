package com.transport.transport_system.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.transport.transport_system.model.Schedule;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findByDriverId(Long driverId);
    List<Schedule> findByDriverEmail(String driverEmail);
}