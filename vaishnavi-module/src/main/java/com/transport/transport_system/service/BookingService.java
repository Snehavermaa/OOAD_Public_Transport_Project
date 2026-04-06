package com.transport.transport_system.service;

import com.transport.transport_system.model.Booking;
import com.transport.transport_system.model.Schedule;
import com.transport.transport_system.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ScheduleService scheduleService;

    public Booking createBooking(Booking booking, Long scheduleId) {
        Schedule schedule = scheduleService.getScheduleById(scheduleId);
        booking.setSchedule(schedule);
        booking.setStatus("CONFIRMED");
        booking.setBookingReference(BookingManager.getInstance().registerBooking(booking));
        return bookingRepository.save(booking);
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public Booking getBookingById(Long id) {
        return bookingRepository.findById(id).orElse(null);
    }

    public Booking cancelBooking(Long bookingId) {
        Booking booking = getBookingById(bookingId);
        if (booking != null && !"CANCELLED".equals(booking.getStatus())) {
            booking.setStatus("CANCELLED");
            BookingManager.getInstance().cancelBooking(booking.getBookingReference());
            return bookingRepository.save(booking);
        }
        return booking;
    }
}