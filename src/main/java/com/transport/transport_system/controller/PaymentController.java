package com.transport.transport_system.controller;

import com.transport.transport_system.model.Booking;
import com.transport.transport_system.model.Transaction;
import com.transport.transport_system.model.User;
import com.transport.transport_system.service.BookingService;
import com.transport.transport_system.service.FareService;
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
import jakarta.servlet.http.HttpSession;

@Controller
public class PaymentController {

    private final PaymentService paymentService;
    private final BookingService bookingService;
    private final FareService fareService;

    @Autowired
    public PaymentController(PaymentService paymentService, BookingService bookingService, FareService fareService) {
        this.paymentService = paymentService;
        this.bookingService = bookingService;
        this.fareService = fareService;
    }

    @GetMapping("/payment/new")
    public String showPaymentForm(@RequestParam Long bookingId, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }
        Booking booking = bookingService.getBookingById(bookingId);
        if (booking == null) {
            return "redirect:/bookings";
        }
        
        double distance = booking.getSchedule() != null && booking.getSchedule().getRoute() != null
                ? booking.getSchedule().getRoute().getDistance()
                : 0.0;
        double perSeat = fareService.calculateBaseFarePerSeat(distance);
        double subtotal = booking.getSeats() * perSeat;
        double tax = fareService.calculateTax(subtotal);
        double amount = fareService.calculateTotalWithTax(subtotal);
        
        model.addAttribute("bookingId", bookingId);
        model.addAttribute("seats", booking.getSeats());
        model.addAttribute("perSeatFare", perSeat);
        model.addAttribute("subtotal", subtotal);
        model.addAttribute("tax", tax);
        model.addAttribute("taxRatePercent", (int) (fareService.getTaxRate() * 100));
        model.addAttribute("amount", amount);
        return "payment-form";
    }

    @PostMapping("/payment/process")
    public String processPayment(@RequestParam Long bookingId,
                                 @RequestParam double amount,
                                 @RequestParam String method,
                                 @RequestParam(required = false) String detail,
                                 HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }
        
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
        if ("SUCCESS".equalsIgnoreCase(transaction.getStatus())) {
            return "redirect:/bookings?confirmed=true";
        }
        return "redirect:/payment/new?bookingId=" + bookingId + "&failed=true";
    }
}
