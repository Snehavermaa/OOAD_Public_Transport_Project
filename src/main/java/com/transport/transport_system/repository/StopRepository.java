package com.transport.transport_system.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.transport.transport_system.model.Stop;


public interface StopRepository extends JpaRepository<Stop, Long> {
    List<Stop> findByRouteId(Long routeId);
}
