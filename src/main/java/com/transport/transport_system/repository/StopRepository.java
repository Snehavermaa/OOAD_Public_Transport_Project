package com.transport.transport_system.repository;

import com.transport.transport_system.model.Stop;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StopRepository extends JpaRepository<Stop, Long> {
}