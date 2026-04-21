package com.transport.transport_system.repository;

import com.transport.transport_system.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByPassengerEmail(String passengerEmail);

    @Query("SELECT b.schedule.route.source, b.schedule.route.destination, COUNT(b) " +
            "FROM Booking b WHERE b.schedule IS NOT NULL AND b.schedule.route IS NOT NULL " +
            "GROUP BY b.schedule.route.source, b.schedule.route.destination")
    List<Object[]> countBookingsGroupedByRoute();

    @Query("SELECT b.schedule.busId, COUNT(b) " +
            "FROM Booking b WHERE b.schedule IS NOT NULL AND b.schedule.busId IS NOT NULL " +
            "GROUP BY b.schedule.busId")
    List<Object[]> countBookingsGroupedByBus();
}