package com.transport.transport_system.strategy;

public class UPIPayment implements PaymentStrategy {
    
    private String upiId;
    
    public UPIPayment(String upiId) {
        this.upiId = upiId;
    }

    @Override
    public boolean pay(double amount) {
        // Simulate payment gateway logic for UPI
        System.out.println("Processing UPI payment of $" + amount + " to ID: " + upiId);
        return true; // Assume success for simulation
    }

    @Override
    public String getPaymentMethodName() {
        return "UPI";
    }
}
