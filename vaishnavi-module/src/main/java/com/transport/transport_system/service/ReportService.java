package com.transport.transport_system.service;

import com.transport.transport_system.model.Booking;
import com.transport.transport_system.model.Transaction;
import com.transport.transport_system.repository.BookingRepository;
import com.transport.transport_system.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportService {

    private final TransactionRepository transactionRepository;
    private final BookingRepository bookingRepository;

    @Autowired
    public ReportService(TransactionRepository transactionRepository, BookingRepository bookingRepository) {
        this.transactionRepository = transactionRepository;
        this.bookingRepository = bookingRepository;
    }

    public List<Booking> getBookingHistory() {
        return bookingRepository.findAll();
    }

    public List<Transaction> getTransactionHistory() {
        return transactionRepository.findAll();
    }

    public double getTotalRevenue() {
        return transactionRepository.getTotalRevenue();
    }

    public Map<String, Double> getRevenueByPaymentMethod() {
        List<Transaction> transactions = transactionRepository.findAll();
        Map<String, Double> breakdown = new HashMap<>();
        
        for (Transaction t : transactions) {
            if ("SUCCESS".equals(t.getStatus())) {
                breakdown.put(t.getPaymentMethod(), 
                              breakdown.getOrDefault(t.getPaymentMethod(), 0.0) + t.getAmount());
            }
        }
        return breakdown;
    }
}
