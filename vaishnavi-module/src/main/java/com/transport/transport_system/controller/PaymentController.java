package com.transport.transport_system.controller;

import com.transport.transport_system.model.Booking;
import com.transport.transport_system.model.Transaction;
import com.transport.transport_system.service.BookingService;
import com.transport.transport_system.service.PaymentService;
import com.transport.transport_system.strategy.CreditCardPayment;
import com.transport.transport_system.strategy.PaymentContext;
import com.transport.transport_system.strategy.UPIPayment;
import com.transport.transport_system.strategy.WalletPayment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PaymentController {

    private final PaymentService paymentService;
    private final BookingService bookingService;

    @Autowired
    public PaymentController(PaymentService paymentService, BookingService bookingService) {
        this.paymentService = paymentService;
        this.bookingService = bookingService;
    }

    @GetMapping("/payment/new")
    public String showPaymentForm(@RequestParam Long bookingId, Model model) {
        Booking booking = bookingService.getBookingById(bookingId);
        if (booking == null) {
            return "redirect:/bookings";
        }
        
        // Simple fare hardcoding based on seats since FareService might be complex to integrate here
        double amount = booking.getSeats() * 50.0; // Assuming 50 per seat as flat rate for simplicity
        
        model.addAttribute("bookingId", bookingId);
        model.addAttribute("amount", amount);
        return "payment-form";
    }

    @PostMapping("/payment/process")
    public String processPayment(@RequestParam Long bookingId,
                                 @RequestParam double amount,
                                 @RequestParam String method,
                                 @RequestParam(required = false) String detail,
                                 Model model) {
        
        PaymentContext context = new PaymentContext();
        
        switch (method) {
            case "CreditCard":
                context.setPaymentStrategy(new CreditCardPayment(detail, "123", "12/25"));
                break;
            case "UPI":
                context.setPaymentStrategy(new UPIPayment(detail));
                break;
            case "Wallet":
                context.setPaymentStrategy(new WalletPayment(detail));
                break;
            default:
                throw new IllegalArgumentException("Unknown payment method");
        }
        
        Transaction transaction = paymentService.processPayment(bookingId, context, amount);
        
        model.addAttribute("transaction", transaction);
        return "payment-success";
    }
}
