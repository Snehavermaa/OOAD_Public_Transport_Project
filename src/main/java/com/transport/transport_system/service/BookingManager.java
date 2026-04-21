package com.transport.transport_system.service;

import com.transport.transport_system.model.Booking;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class BookingManager {

    private static final BookingManager INSTANCE = new BookingManager();
    private final AtomicInteger nextReference = new AtomicInteger(1000);
    private final Map<String, Booking> activeBookings = Collections.synchronizedMap(new HashMap<>());

    private BookingManager() {
    }

    public static BookingManager getInstance() {
        return INSTANCE;
    }

    public String registerBooking(Booking booking) {
        String reference = "BK-" + nextReference.getAndIncrement();
        booking.setBookingReference(reference);
        booking.setStatus("CONFIRMED");
        activeBookings.put(reference, booking);
        return reference;
    }

    public Booking cancelBooking(String bookingReference) {
        Booking booking = activeBookings.remove(bookingReference);
        if (booking != null) {
            booking.setStatus("CANCELLED");
        }
        return booking;
    }

    public Booking getBooking(String bookingReference) {
        return activeBookings.get(bookingReference);
    }
}