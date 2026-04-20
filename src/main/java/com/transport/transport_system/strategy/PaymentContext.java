package com.transport.transport_system.strategy;

public class PaymentContext {
    private PaymentStrategy paymentStrategy;

    public void setPaymentStrategy(PaymentStrategy paymentStrategy) {
        this.paymentStrategy = paymentStrategy;
    }

    public boolean executePayment(double amount) {
        if (paymentStrategy == null) {
            throw new IllegalStateException("Payment strategy not set");
        }
        return paymentStrategy.pay(amount);
    }
    
    public String getPaymentMethodName() {
        if (paymentStrategy == null) {
            return "Unknown";
        }
        return paymentStrategy.getPaymentMethodName();
    }
}
