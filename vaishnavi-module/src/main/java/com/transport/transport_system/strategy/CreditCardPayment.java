package com.transport.transport_system.strategy;

public class CreditCardPayment implements PaymentStrategy {
    
    private String cardNumber;
    private String cvv;
    private String expiryDate;
    
    public CreditCardPayment(String cardNumber, String cvv, String expiryDate) {
        this.cardNumber = cardNumber;
        this.cvv = cvv;
        this.expiryDate = expiryDate;
    }

    @Override
    public boolean pay(double amount) {
        // Simulate payment gateway logic for credit card
        System.out.println("Processing credit card payment of $" + amount + " for card: " + cardNumber);
        return true; // Assume success for simulation
    }

    @Override
    public String getPaymentMethodName() {
        return "Credit Card";
    }
}
