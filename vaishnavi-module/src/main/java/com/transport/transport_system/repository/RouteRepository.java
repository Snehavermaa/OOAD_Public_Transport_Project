package com.transport.transport_system.repository;

import com.transport.transport_system.model.Route;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RouteRepository extends JpaRepository<Route, Long> {
}