package com.transport.transport_system.service;

import com.transport.transport_system.model.Booking;
import com.transport.transport_system.model.Transaction;
import com.transport.transport_system.repository.BookingRepository;
import com.transport.transport_system.repository.TransactionRepository;
import com.transport.transport_system.strategy.PaymentContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class PaymentService {

    private final TransactionRepository transactionRepository;
    private final BookingRepository bookingRepository;

    @Autowired
    public PaymentService(TransactionRepository transactionRepository, BookingRepository bookingRepository) {
        this.transactionRepository = transactionRepository;
        this.bookingRepository = bookingRepository;
    }

    public Transaction processPayment(Long bookingId, PaymentContext paymentContext, double amount) {
        // Assume retrieving user ID implicitly or defaulting to 1L for this mini-project
        Long userId = 1L; 

        // Execute payment strategy
        boolean success = paymentContext.executePayment(amount);
        String status = success ? "SUCCESS" : "FAILED";

        // Save transaction record via JDBC
        Transaction transaction = new Transaction(userId, bookingId, amount, paymentContext.getPaymentMethodName(), status, new Date());
        transactionRepository.save(transaction);

        // Update booking status if successful
        if (success) {
            Booking booking = bookingRepository.findById(bookingId).orElse(null);
            if (booking != null) {
                booking.setStatus("CONFIRMED");
                bookingRepository.save(booking);
            }
        }

        return transaction;
    }
}
